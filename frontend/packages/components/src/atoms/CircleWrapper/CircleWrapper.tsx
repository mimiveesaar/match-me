"use client";

import React from "react";
import { ReactNode, CSSProperties } from "react";

interface CircleWrapperProps {
  children: ReactNode;
  className?: string;
}

export const CircleWrapper = ({
  children,
  className = "",
}: CircleWrapperProps) => {
  return (
    <div
      className={`flex items-center justify-center rounded-full shadow-lg ${className}`}
    >
      {children}
    </div>
  );
};