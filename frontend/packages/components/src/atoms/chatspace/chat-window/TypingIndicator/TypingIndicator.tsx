import React from "react";

interface TypingIndicatorProps {
  isLeft?: boolean; // true if from other user, false if from self
}

export const TypingIndicator = ({ isLeft = true }: TypingIndicatorProps) => {
  const alignment = isLeft ? "justify-start" : "justify-end";
  const bubbleColor = isLeft ? "bg-ivory" : "bg-amberglow";

  return (
    <div className={`flex ${alignment} mb-2`}>
      <div className={`${bubbleColor} rounded-custom-16 px-4 py-2 flex items-center gap-1 drop-shadow-custom-2`}>
        <span className="w-1 h-1 bg-black/80 rounded-full animate-bounce"></span>
        <span className="w-1 h-1 bg-black/80 rounded-full animate-bounce delay-150"></span>
        <span className="w-1 h-1 bg-black/80 rounded-full animate-bounce delay-300"></span>
      </div>
    </div>
  );
};
