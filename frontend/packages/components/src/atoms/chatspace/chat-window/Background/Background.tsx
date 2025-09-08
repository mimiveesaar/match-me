"use client";

import { ReactNode } from "react";

interface BackgroundProps {
  children: ReactNode;
}

export const Background = ({ children }: BackgroundProps) => {
  return (
    <div className="flex justify-center items-center px-4">
      <div
        className="w-[643px] h-[542px] rounded-custom-16 inner-shadow-custom bg-peony/90 overflow-auto"
      >
        {children}
      </div>
    </div>
  );
};