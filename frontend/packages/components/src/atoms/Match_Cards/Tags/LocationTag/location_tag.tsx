"use client";

import React, { ReactNode } from "react";

interface LocationTagProps {
  location: string;
  className?: string;
};

export const LocationTag = ({ location, className = "" }: LocationTagProps) => {
  
  return (
    <span className={`h-17 inline-flex items-center gap-2 bg-ivory-90 text-black text-xs font-medium font-serif-text px-2 rounded-custom-4 ${className}`}>
      📍 {location}
    </span>
  );
}