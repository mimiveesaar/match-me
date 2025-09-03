"use client";

import React, { TextareaHTMLAttributes } from "react";

interface BackgroundProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  rightElement?: React.ReactNode;
}

export const Background = ({ rightElement, ...props }: BackgroundProps) => {
  return (
    <div className="w-full max-w-[604px] h-[73px] rounded-custom-16 border border-black/70 px-4 py-2 bg-transparent flex items-center gap-2">
      <textarea
        {...props}
        className="resize-none flex-1 h-full bg-transparent text-xs text-black outline-none focus:outline-none pr-2"
      />
      {rightElement && <div className="flex-shrink-0">{rightElement}</div>}
    </div>
  );
};