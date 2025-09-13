"use client";

import React from "react";
import { ReactNode, CSSProperties } from "react";

interface CircleWrapperProps {
  children: ReactNode;
  className?: string;
  style?: CSSProperties;
}

export const CircleWrapper = ({
  children,
  className = "",
  style
}: CircleWrapperProps) => {
  return (
    <div
      className={`flex items-center justify-center rounded-full shadow-lg ${className}`}
      style={style}
    >
      {children}
    </div>
  );
};