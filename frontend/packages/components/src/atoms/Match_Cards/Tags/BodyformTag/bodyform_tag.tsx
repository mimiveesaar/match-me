"use client";

import React from "react";

interface BodyformTagProps {
  bodyform: string;
  className?: string;
};

export const BodyformTag = ({ bodyform, className = "" }: BodyformTagProps) => {
  
  return (
    <span className={`h-17 inline-flex items-center gap-2 bg-ivory-90 text-black text-xs font-medium font-serif-text px-2 rounded-custom-4 ${className}`}>
      🪲 {bodyform}
    </span>
  );
}