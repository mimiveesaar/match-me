"use client";

import { AlienMeetLogo } from "@components/atoms/Alien.meet logo/alien_meet";
import { Menu } from "@components/organisms/Menu/menu";
import { ConnectionsMenu } from "@components/organisms/chatspace/Connections Menu/ConnectionsMenu";
import { ChatWindow } from "@components/organisms/chatspace/Chat Window/ChatWindow";
import { MessageInput } from "@components/organisms/chatspace/Message Input/MessageInput";



export default function Chatspace() {
  const userId = "d87e7304-7bfb-4bfb-9318-52c58f3c1034"; // TODO: replace with real logged-in user later

  return (
    <div className="flex flex-col min-h-screen bg-gray-950 text-white">
      {/* Header / Logo */}
      <header className="flex justify-center py-4 border-b border-gray-800">
        <AlienMeetLogo />
      </header>

      {/* Main content row */}
      <main className="flex flex-1 w-full max-w-7xl mx-auto">
        {/* Left sidebar */}
        <aside className="w-64 border-r border-gray-800 p-4">
          <Menu />
        </aside>

        {/* Chat section */}
        <section className="flex flex-col flex-1 border-r border-gray-800">
          <div className="flex-1 overflow-y-auto">
            <ChatWindow />
          </div>
          <div className="border-t border-gray-800 p-4">
            <MessageInput onSend={function (message: string): void {
              throw new Error("Function not implemented.");
            } } />
          </div>
        </section>

        {/* Right sidebar */}
        <aside className="w-80 p-4">
          <ConnectionsMenu username={""} />
        </aside>
      </main>
    </div>
  );
}