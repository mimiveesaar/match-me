"use client";

import { ReactNode } from "react";

interface CircleWrapperProps {
  children: ReactNode;
  size?: string;
  className?: string;
  backgroundColor?: string; // optional background
}

export const CircleWrapper = ({
  children,
  size = "w-40 h-40",
  className = "",
  backgroundColor = "bg-blue",
}: CircleWrapperProps) => {
  return (
    <div
      className={`rounded-full shadow-lg flex items-center justify-center ${size} ${backgroundColor} ${className}`}
    >
      {children}
    </div>
  );
};
