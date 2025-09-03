"use client";

import {SpeechBubble} from "@atoms/CHATSPACE/Chat Window/Speech Bubble/SpeechBubble";
import {MessageHeader} from "@atoms/CHATSPACE/Chat Window/Message Header/MessageHeader";

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