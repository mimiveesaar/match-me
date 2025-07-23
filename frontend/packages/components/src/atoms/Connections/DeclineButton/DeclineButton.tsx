"use client";

import React, { ReactNode } from "react";
import { X } from "lucide-react";

interface DeclineButtonProps {
  className?: string;
}

export const DeclineButton = ({ className = "" }: DeclineButtonProps) => {
  return (
    <button
      type="button"
      className="inline-flex cursor-pointer items-center justify-center transition-transform hover:scale-110"
      onClick={() => alert(`Are you sure you want to decline this connection?`)}
    >
      <X className={`${className}`} />
    </button>
  );
};
