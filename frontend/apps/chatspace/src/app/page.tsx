"use client";

import { AlienMeetLogo } from "@components/atoms/Alien.meet logo/alien_meet";
import { Menu } from "@components/organisms/Menu/menu";
import { ConnectionsMenu } from "@components/organisms/chatspace/Connections Menu/ConnectionsMenu";
import { ChatWindow } from "@components/organisms/chatspace/Chat Window/ChatWindow";
import { MessageInput } from "@components/organisms/chatspace/Message Input/MessageInput";
import { useChat } from "../../hooks/useChat";
import { useEffect, useState } from "react";
import axios from "axios";


export default function Chatspace() {

  const [users, setUsers] = useState<Array<{ id: string, username: string, isOnline: boolean }>>([]);
  const [conversationId, setConversationId] = useState<string | null>(null);

  const search = typeof window !== 'undefined' ? window.location.search.toLowerCase() : '';
  const userId = search.includes('userb')
    ? '33333333-3333-3333-3333-333333333333'
    : '11111111-1111-1111-1111-111111111111';

  const senderUsername = search.includes('userb') ? 'mike' : 'Henry';

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
        // 1️⃣ Fetch user connections
        const response = await axios.get(
          `http://localhost:8080/api/users/${userId}/connections`
        );
        const connections = response.data.map((user: UserConnection) => ({
          id: user.id,
          username: user.username,
          isOnline: user.status === "ONLINE",
        }));
        setUsers(connections);

        // 2️⃣ Fetch all conversations for the logged-in user
        const convosRes = await axios.get(
          `http://localhost:8080/api/conversations/user/${userId}`
        );
        const conversations = convosRes.data;

        // 3️⃣ Automatically open the latest conversation
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
          <Menu />
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