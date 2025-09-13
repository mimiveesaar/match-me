"use client";

import { AlienMeetLogo } from "@components/atoms/Alien.meet logo/alien_meet";
import { Menu } from "@components/organisms/Menu/menu";
import { ConnectionsMenu } from "@components/organisms/chatspace/Connections Menu/ConnectionsMenu";
import { ChatWindow } from "@components/organisms/chatspace/Chat Window/ChatWindow";
import { MessageInput } from "@components/organisms/chatspace/Message Input/MessageInput";
import { useChat } from "../../hooks/useChat";
import { useEffect, useState } from "react";
import axios from "axios";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";


export default function Chatspace() {

  const [users, setUsers] = useState<Array<{ id: string, username: string, isOnline: boolean }>>([]);
  const [conversationId, setConversationId] = useState<string | null>(null);
  const [hasUnread, setHasUnread] = useState(false);

  const search = typeof window !== 'undefined' ? window.location.search.toLowerCase() : '';
  const userId = search.includes('userb')
    ? '33333333-3333-3333-3333-333333333333'
    : '11111111-1111-1111-1111-111111111111';

  const senderUsername = search.includes('userb') ? 'mike' : 'Henry';

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");

    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      connectHeaders: { userId },
      onConnect: () => {
        console.log("Connected as user:", userId);

        stompClient.subscribe(`/topic/status/${userId}`, (message) => {
          console.log("Status update:", message.body);
        });

        stompClient.subscribe(`/topic/unread/${userId}`, (msg) => {
          const payload = JSON.parse(msg.body);
          console.log("🔴 STOMP /topic/unread payload:", payload);

          // If the user is already viewing this conversation, skip the dot
          if (payload.conversationId === conversationId) {
            console.log("✅ Skipping dot because user is already in this conversation");
            return;
          }

          setHasUnread(payload.hasUnread);
        });
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"], frame.body);
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [userId]); // runs once per userId


  const { messages, setMessages, sendMessage, handleTyping, otherUserTyping } = useChat(conversationId ?? '', userId, senderUsername);
  const [input, setInput] = useState('');

  interface UserConnection {
    id: string;
    username: string;
    status: string;
  }

  useEffect(() => {

    async function fetchConnectionsAndConversations() {
      try {
        // Fetch user connections
        const response = await axios.get(
          `http://localhost:8080/api/users/${userId}/connections`
        );
        const connections = response.data.map((user: UserConnection) => ({
          id: user.id,
          username: user.username,
          isOnline: user.status === "ONLINE",
        }));
        setUsers(connections);

        // Fetch all conversations for the logged-in user
        const convosRes = await axios.get(
          `http://localhost:8080/api/conversations/user/${userId}`
        );
        const conversations = convosRes.data;

        // Automatically open the latest conversation
        if (conversations.length > 0) {
          const latestConvo = conversations[0]; // already sorted by latest message
          setConversationId(latestConvo.id);
          setMessages(latestConvo.messages || []);
        }

      } catch (err) {
        console.error("Failed to fetch connections or conversations:", err);

        // Fallback: mock connections
        const mockConnections =
          userId === "11111111-1111-1111-1111-111111111111"
            ? [{ id: "33333333-3333-3333-3333-333333333333", username: "mike", isOnline: true }]
            : [{ id: "11111111-1111-1111-1111-111111111111", username: "Henry", isOnline: true }];
        setUsers(mockConnections);
      }
    }

    fetchConnectionsAndConversations();
  }, [userId, senderUsername]);

  useEffect(() => {
    if (!conversationId || !userId) return;

    const markAsRead = async () => {
      try {
        await axios.put(
          `http://localhost:8080/api/conversations/${conversationId}/read`,
          null,
          { params: { userId } }
        );

        // Optimistically update local messages
        setMessages((prev) =>
          prev.map((m) =>
            m.senderId !== userId ? { ...m, status: "READ" } : m
          )
        );
      } catch (err) {
        console.error("Failed to mark conversation as read", err);
      }
    };

    markAsRead();
  }, [conversationId, userId]);

  useEffect(() => {
    console.log("🔹 hasUnread state changed:", hasUnread);
  }, [hasUnread]);

  return (
    <div className="flex flex-col h-screen bg-ivory">
      {/* Header */}
      <header className="flex justify-center py-4 flex-shrink-0">
        <AlienMeetLogo />
      </header>

      {/* Main content */}
      <main className="flex w-full max-w-7xl mx-auto">
        {/* Left sidebar */}
        <aside className="w-80 flex items-center justify-center mt-20">
          <Menu hasUnread={hasUnread} />
        </aside>

        {/* Chat section */}
        <section className="flex flex-col flex-1 relative">
          {/* Chat messages */}
          <div className="flex-1 mt-32">
            <ChatWindow messages={messages} currentUserId={userId} otherUserTyping={otherUserTyping} />
          </div>

          {/* Message input */}
          <div className="p-4 flex-shrink-0">
            <MessageInput
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onSend={(msg) => {
                sendMessage(msg);
                setInput('');
              }}
              onTyping={handleTyping}
            />
          </div>
        </section>

        {/* Right sidebar */}
        <aside className="w-72 mt-32 flex flex-col">
          <ConnectionsMenu
            users={users}
            onSelectUser={async (otherUserId) => {
              try {
                console.log(`Opening conversation: ${userId} -> ${otherUserId}`); // Debug log
                const response = await axios.post(`http://localhost:8080/api/conversations/open`, null, {
                  params: { userId, otherUserId },
                });
                const convo = response.data;
                console.log('Conversation opened:', convo); // Debug log
                setConversationId(convo.id);
                setMessages(convo.messages || []); // Ensure messages is array
              } catch (err) {
                console.error('Failed to open conversation:', err);
              }
            }}
          />
        </aside>
      </main>
    </div>
  );
}