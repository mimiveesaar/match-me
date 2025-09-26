"use client";

import React, {
  createContext,
  useContext,
  useEffect,
  useRef,
  useState,
  ReactNode,
} from "react";
import { Client, IMessage, StompSubscription } from "@stomp/stompjs";
import { getStompClient } from "./stompClient";

interface TypingState {
  [conversationId: string]: {
    [userId: string]: boolean;
  };
}

interface WebSocketContextType {
  client: Client | null;
  isConnected: boolean;
  onlineUsers: Set<string>;
  typingState: TypingState;
  setTypingState: React.Dispatch<React.SetStateAction<TypingState>>;
  hasUnread: boolean;
  setHasUnread: React.Dispatch<React.SetStateAction<boolean>>;
  subscribeToUnread: (
    userId: string,
    currentConversationId: string | null,
  ) => StompSubscription;
  isUserOnline: (userId: string) => boolean;
}

const WebSocketContext = createContext<WebSocketContextType | undefined>(
  undefined,
);

interface Props {
  userId: string;
  children: ReactNode;
}

export const WebSocketProvider = ({ userId, children }: Props) => {
  const [client, setClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState(false);
  const [onlineUsers, setOnlineUsers] = useState<Set<string>>(new Set());
  const [typingState, setTypingState] = useState<TypingState>({});
  const [hasUnread, setHasUnread] = useState(false);

  const statusSubRef = useRef<StompSubscription | null>(null);

  const subscribeToUnread = (
    userId: string,
    currentConversationId: string | null,
  ) => {
    if (!client) return {} as StompSubscription;
    return client.subscribe(`/topic/unread/${userId}`, (msg: IMessage) => {
      const payload = JSON.parse(msg.body);
      if (payload.conversationId !== currentConversationId) {
        setHasUnread(payload.hasUnread);
      }
    });
  };

  const isUserOnline = (id: string) => {
    return onlineUsers.has(id);
  };

  useEffect(() => {
    const stompClient = getStompClient(userId);
    setClient(stompClient);

    stompClient.onConnect = async () => {
      console.log("[WebSocketContext] Connected");
      setIsConnected(true);

      statusSubRef.current = stompClient.subscribe(
        "/topic/status",
        (msg: IMessage) => {
          const payload = JSON.parse(msg.body);
          const { userId: affectedUserId, status } = payload;

          setOnlineUsers((prev) => {
            const updated = new Set(prev);
            if (status === "ONLINE") {
              updated.add(affectedUserId);
            } else {
              updated.delete(affectedUserId);
            }
            console.log(`[Presence] ${affectedUserId} is now ${status}`);
            return updated;
          });
        },
      );

      try {
        const res = await fetch(
          "http://localhost:8080/api/presence/online-users",
        );
        const currentlyOnline: string[] = await res.json();

        setOnlineUsers((prev) => {
          const updated = new Set(prev);
          currentlyOnline.forEach((id) => updated.add(id));
          return updated;
        });

        console.log(
          "[Presence] Fetched online users from server:",
          currentlyOnline,
        );
      } catch (err) {
        console.error("[Presence] Failed to fetch current online users:", err);
      }
    };

    stompClient.onStompError = (frame) => {
      console.error("[WebSocketContext] STOMP Error", frame);
    };

    stompClient.activate();

    return () => {
      console.log("[WebSocketContext] Cleaning up...");
      statusSubRef.current?.unsubscribe();
      stompClient.deactivate();
    };
  }, [userId]);

  return (
    <WebSocketContext.Provider
      value={{
        client,
        isConnected,
        onlineUsers,
        typingState,
        setTypingState,
        hasUnread,
        setHasUnread,
        subscribeToUnread,
        isUserOnline,
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = (): WebSocketContextType => {
  const context = useContext(WebSocketContext);
  if (!context) {
    throw new Error("useWebSocket must be used within a WebSocketProvider");
  }
  return context;
};
