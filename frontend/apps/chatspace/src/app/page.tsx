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
  const userId = '11111111-1111-1111-1111-111111111111'; // current user UUID

  const { messages, sendMessage } = useChat(conversationId, userId);
  const [input, setInput] = useState('');

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
            <ChatWindow messages={messages} currentUserId={userId} />
          </div>

          {/* Message input */}
          <div className="p-4 flex-shrink-0">
            <MessageInput
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onSend={() => {
                if (input.trim()) {
                  sendMessage(input.trim());
                  setInput('');
                }
              }}
            />
          </div>
        </section>

        {/* Right sidebar */}
        <aside className="w-72 mt-32 flex flex-col">
          <ConnectionsMenu username="Shelly" />
        </aside>
      </main>
    </div>
  );
}