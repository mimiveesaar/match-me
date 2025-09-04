"use client";

import { SpeechBubble } from "@atoms/chatspace/chat-window/Speech Bubble/SpeechBubble";
import { MessageHeader } from "@atoms/chatspace/chat-window/Message Header/MessageHeader";

export const SpeechBubbleWithHeader = ({
  sender,
  time,
  date,
  bubbleColor,
  children,
}: {
  sender: string;
  time: string;
  date: string;
  bubbleColor?: "bg-amberglow" | "bg-ivory";
  children: React.ReactNode;
}) => {
  return (
    <div className="flex flex-col space-y-0.5">
      <MessageHeader sender={sender} time={time} date={date} />
      <SpeechBubble color={bubbleColor}>{children}</SpeechBubble>
    </div>
  );
}