"use client";

import { Background } from "@atoms/chatspace/chat-window/Background/Background";
import { SpeechBubbleWithHeader } from "@molecules/chatspace/chat-window/speech-bubble-with-header/SpeechBubbleWithHeader";
import { ChatMessage, ChatWindowProps } from "/workspace/frontend/packages/components/src/types";


export const ChatWindow = ({ messages, currentUserId }: ChatWindowProps) => {
  const formatTime = (timestamp: string) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  const formatDate = (timestamp: string) => {
    const date = new Date(timestamp);
    return date.toLocaleDateString();
  };

  return (
    <Background>
      <div className="flex flex-col space-y-4 p-4">
        {messages.map((msg, idx) => {
          const isMe = msg.senderId === currentUserId;
          return (
            <div key={idx} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
              <SpeechBubbleWithHeader
                sender={isMe ? 'Me' : msg.senderId}
                time={formatTime(msg.timestamp)}
                date={formatDate(msg.timestamp)}
                bubbleColor={isMe ? 'bg-amberglow' : 'bg-ivory'}
              >
                {msg.content}
              </SpeechBubbleWithHeader>
            </div>
          );
        })}
      </div>
    </Background>
  );
};