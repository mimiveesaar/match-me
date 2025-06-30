"use client";

import React, { ReactNode } from "react";

interface CardProps {
  children: ReactNode;
  className?: string;
}

export const MatchCardBackground = ({
  children,
  className = "",
}: CardProps) => {
  return (
    <div className={`w-265 h-360 rounded-custom-16 drop-shadow-custom ${className}`}>
      {children}
    </div>
  );
};
