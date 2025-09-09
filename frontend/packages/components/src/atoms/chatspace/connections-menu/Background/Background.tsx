"use client";

import { ReactNode } from "react";

interface BackgroundProps {
  children: ReactNode;
}

export const Background = ({ children }: BackgroundProps) => {
  return (
    <div className="flex justify-center items-center px-4">
      <div
        className="h-full max-w-[204px] min-h-[640px] rounded-custom-16 drop-shadow-custom bg-olive/90 overflow-auto"
      >
        {children}
      </div>
    </div>
  );
};