"use client";

import { AlienMeetLogo } from "components";
import { Menu } from "components/organisms";
import { ConnectionsMenu } from "components/organisms";
import { ChatWindow } from "components/organisms";
import { MessageInput } from "components/organisms";
import { useChat } from "./hooks/useChat";
import { useEffect, useState } from "react";
import axios from "axios";
import { getStompClient } from "./utils/stompClient";


export default function Chatspace() {

  const [users, setUsers] = useState<Array<{ id: string, username: string, isOnline: boolean }>>([]);
  const [conversationId, setConversationId] = useState<string | null>(null);
  const [hasUnread, setHasUnread] = useState(false);

  const search = typeof window !== 'undefined' ? window.location.search.toLowerCase() : '';
  const userId = search.includes('userb')
    ? '22222222-2222-2222-2222-222222222222'
    : '11111111-1111-1111-1111-111111111111';

  const senderUsername = search.includes('userb') ? 'bob_cosmic' : 'alice_space';

    useEffect(() => {
        const client = getStompClient(userId);

        const onConnect = () => {
            console.log("✅ STOMP connected, subscribing for", userId);

            const statusSub = client.subscribe(`/topic/status/${userId}`, (message) => {
                console.log("Status update:", message.body);
            });

            const unreadSub = client.subscribe(`/topic/unread/${userId}`, (msg) => {
                const payload = JSON.parse(msg.body);
                console.log("🔴 STOMP /topic/unread payload:", payload);

                if (payload.conversationId === conversationId) {
                    console.log("✅ Skipping dot because user is already in this conversation");
                    return;
                }

                setHasUnread(payload.hasUnread);
            });

            // Optional: clean up subs on unmount
            cleanup = () => {
                console.log("[STOMP CLEANUP] Unsubscribing from global topics");
                statusSub.unsubscribe();
                unreadSub.unsubscribe();
            };
        };

        // Register connect callback
        client.onConnect = onConnect;

        // Optional error logging
        client.onStompError = (frame) => {
            console.error("STOMP error:", frame.headers['message'], frame.body);
        };

        let cleanup = () => {};

        return () => {
            cleanup();
        };
    }, [userId, conversationId]);
// runs once per userId


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
            ? [{ id: "22222222-2222-2222-2222-222222222222", username: "bob_cosmic", isOnline: true }]
            : [{ id: "11111111-1111-1111-1111-111111111111", username: "alice_space", isOnline: true }];
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