"use client";

import { ReactNode } from "react";

interface BackgroundProps {
  children: ReactNode;
}

export const Background = ({ children }: BackgroundProps) => {
  return (
    <div className="flex justify-center items-center min-h-screen px-4">
      <div
        className="h-full max-w-[204px] max-h-[650px] rounded-custom-16 drop-shadow-custom bg-olive/90 overflow-auto"
      >
        {children}
      </div>
    </div>
  );
};