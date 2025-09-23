"use client";

import { MessageHeader, SpeechBubble } from "src/atoms";

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
  const isAmberglow = bubbleColor === "bg-amberglow"

  return (
    <div className="flex flex-col space-y-0.5">
      <div className={isAmberglow ? "flex justify-end pr-7" : ""}>
        <MessageHeader sender={sender} time={time} date={date} />
      </div>
      <SpeechBubble color={bubbleColor}>{children}</SpeechBubble>
    </div>
  );
};