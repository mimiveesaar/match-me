"use client";

import { SpeechBubble } from "../SpeechBubble/SpeechBubble";


interface TypingIndicatorProps {
  isLeft?: boolean; // true if from other user, false if from self
}

export const TypingIndicator = ({ isLeft = true }: TypingIndicatorProps) => {
  const alignment = isLeft ? "justify-start" : "justify-end";
  const bubbleColor = isLeft ? "bg-ivory" : "bg-amberglow";

  return (
    <div className={`flex ${alignment} mb-2`}>
      <SpeechBubble color={bubbleColor as "bg-ivory" | "bg-amberglow"}>
        <div className="flex items-center gap-1">
          <span className="w-1.5 h-1.5 bg-black/80 rounded-full animate-bounce"></span>
          <span className="w-1.5 h-1.5 bg-black/80 rounded-full animate-bounce delay-150"></span>
          <span className="w-1.5 h-1.5 bg-black/80 rounded-full animate-bounce delay-300"></span>
        </div>
      </SpeechBubble>
    </div>
  );
};
