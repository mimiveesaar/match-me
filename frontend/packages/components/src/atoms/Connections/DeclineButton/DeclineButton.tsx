"use client";

import React, { ReactNode } from "react";
import { X } from "lucide-react";

export interface DeclineButtonProps {
  className?: string;
}

export const DeclineButton = ({ className }: DeclineButtonProps) => {
  return (
    <button
      type="button"
      className="mt-1 flex size-5 cursor-pointer items-center justify-center transition hover:scale-125"
      onClick={() => alert(`Are you sure you want to disconnect?`)}
    >
      <X className={`h-5 w-5 ${className}`} />
    </button>
  );
};

export const DeclineButtonRed = ({ className }: DeclineButtonProps) => {
  return (
    <DeclineButton className={`text-coral hover:text-red-800 ${className}`} />
  );
};

export const DeclineButtonIvory = ({ className }: DeclineButtonProps) => {
  return (
    <DeclineButton className={`text-ivory hover:text-red-500 ${className}`} />
  );
};
