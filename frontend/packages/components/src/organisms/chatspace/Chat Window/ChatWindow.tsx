"use client";

import { Background } from "@atoms/chatspace/chat-window/Background/Background";
import { SpeechBubbleWithHeader } from "@molecules/chatspace/chat-window/speech-bubble-with-header/SpeechBubbleWithHeader";
import { ChatMessage, ChatWindowProps } from "/workspace/frontend/packages/components/src/types";
import { TypingIndicator } from "@atoms/chatspace/chat-window/TypingIndicator/TypingIndicator";

export const ChatWindow = ({
  messages,
  currentUserId,
  otherUserTyping = false, // receive this from parent
}: ChatWindowProps & { otherUserTyping?: boolean }) => {

  const formatTime = (timestamp: string | undefined) => {
    if (!timestamp) return "";
    const date = new Date(timestamp);
    return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  };

  const formatDate = (timestamp: string | undefined) => {
    if (!timestamp) return "";
    const date = new Date(timestamp);
    return date.toLocaleDateString();
  };

  return (
    <Background>
      <div className="flex flex-col space-y-2 p-4">
        {messages.map((msg) => {
          const isCurrentUser = msg.senderId === currentUserId;

          return (
            <div
              key={msg.timestamp + msg.senderId}
              className={`flex ${isCurrentUser ? "justify-end" : "justify-start"}`}
            >
              <SpeechBubbleWithHeader
                sender={isCurrentUser ? "Me" : msg.senderName || "Unknown"}
                time={formatTime(msg.timestamp)}
                date={formatDate(msg.timestamp)}
                bubbleColor={isCurrentUser ? "bg-amberglow" : "bg-ivory"}
              >
                {msg.content}
              </SpeechBubbleWithHeader>
            </div>
          );
        })}

        {/* Typing indicator aligned to the other user */}
        {otherUserTyping && (
          <div className="flex justify-start">
            <TypingIndicator />
          </div>
        )}
      </div>
    </Background>
  );
};