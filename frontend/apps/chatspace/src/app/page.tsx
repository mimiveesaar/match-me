"use client";

import { AlienMeetLogo } from "@components/atoms/Alien.meet logo/alien_meet";
import { Menu } from "@components/organisms/Menu/menu";
import { ConnectionsMenu } from "@components/organisms/chatspace/Connections Menu/ConnectionsMenu";
import { ChatWindow } from "@components/organisms/chatspace/Chat Window/ChatWindow";
import { MessageInput } from "@components/organisms/chatspace/Message Input/MessageInput";
import { useChat } from "../../hooks/useChat";
import { useState } from "react";


export default function Chatspace() {
  const conversationId = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'; // replace with actual conversation UUID
  const userId = typeof window !== 'undefined' && window.location.search.includes('userB')
    ? '22222222-2222-2222-2222-222222222222'
    : '11111111-1111-1111-1111-111111111111';

  const { messages, sendMessage, handleTyping, otherUserTyping } = useChat(conversationId, userId);
  const [input, setInput] = useState('');

  const users = [
    { id: '1111', username: 'Henry', isOnline: true },
    { id: '2222', username: 'Shelly', isOnline: false },
    { id: '3333', username: 'Zorbplat', isOnline: true },
  ];

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
          <ConnectionsMenu users={users} />
        </aside>
      </main>
    </div>
  );
}