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

  const [users, setUsers] = useState([]);
  const [conversationId, setConversationId] = useState<string | null>(null);


  const userId = typeof window !== 'undefined' && window.location.search.includes('userB')
    ? '33333333-3333-3333-3333-333333333333'
    : '11111111-1111-1111-1111-111111111111';

  const { messages, setMessages, sendMessage, handleTyping, otherUserTyping } = useChat(conversationId ?? '', userId);
  const [input, setInput] = useState('');

  interface UserConnection {
    id: string;
    username: string;
    status: string;
  }

  useEffect(() => {
    async function fetchConnections() {
      try {
        // Point to your Spring Boot backend (usually port 8080)
        const response = await axios.get(`http://localhost:8080/api/users/${userId}/connections`);
        const connections = response.data.map((user: UserConnection) => ({
          id: user.id,
          username: user.username,
          isOnline: user.status === 'ONLINE'
        }));
        setUsers(connections);
      } catch (err) {
        console.error('Failed to fetch connections:', err);
      }
    }

    fetchConnections();
  }, [userId]);



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
        <section className="flex flex-col flex-1">
          {/* Chat messages */}
          <div className="flex-1 overflow-y-auto mt-32">
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
                const response = await axios.post(`http://localhost:8080/api/conversations/open`, null, {
                  params: { userId, otherUserId },
                });
                const convo = response.data;
                setConversationId(convo.id);
                setMessages(convo.messages);
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