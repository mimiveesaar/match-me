"use client";

import { Background } from "@atoms/chatspace/chat-window/Background/Background";
import { SpeechBubbleWithHeader } from "@molecules/chatspace/chat-window/speech-bubble-with-header/SpeechBubbleWithHeader";
import { TypingIndicator } from "@atoms/chatspace/chat-window/TypingIndicator/TypingIndicator";
import { ChatWindowProps } from "@/types";

import { useEffect, useRef } from "react";

export const ChatWindow = ({
  messages,
  currentUserId,
  otherUserTyping = false,
}: ChatWindowProps & { otherUserTyping?: boolean }) => {
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Scroll to bottom whenever messages update
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Parse timestamp and return both time and date
  const parseTimestampArray = (arr?: number[]) => {
    if (!arr || arr.length < 6) return { time: "", date: "" };

    const [year, month, day, hour, minute, second] = arr;
    const dateObj = new Date(year, month - 1, day, hour, minute, second);

    return {
      time: dateObj.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
      date: dateObj.toLocaleDateString([], { month: "numeric", day: "numeric", year: "2-digit" }),
    };
  };

  return (
    <Background>
      <div className="flex flex-col space-y-2 p-4 overflow-y-auto h-full pb-16">
        {messages.map((msg) => {
          const isCurrentUser = msg.senderId === currentUserId;
          const { time, date } = parseTimestampArray(msg.timestamp);

          return (
            <div
              key={msg.timestamp + msg.senderId}
              className={`flex ${isCurrentUser ? "justify-end" : "justify-start"}`}
            >
              <SpeechBubbleWithHeader
                sender={isCurrentUser ? "Me" : msg.senderUsername || "Unknown"}
                time={time}
                date={date}
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

        {/* Dummy div to scroll into view */}
        <div ref={messagesEndRef} />
      </div>
    </Background>
  );
};