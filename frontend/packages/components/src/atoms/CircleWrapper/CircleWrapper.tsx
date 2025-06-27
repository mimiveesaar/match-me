"use client";

import React from "react";
import { ReactNode } from "react";

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
      className={`rounded-full shadow-lg flex items-center justify-center ${className}`}
    >
      {children}
    </div>
  );
};
