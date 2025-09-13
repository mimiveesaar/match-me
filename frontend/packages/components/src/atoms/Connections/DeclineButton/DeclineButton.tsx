"use client";

import React, { ReactNode } from "react";
import { X } from "lucide-react";

export interface DeclineButtonProps {
  className?: string;
  onClick?: () => void;
}

export const DeclineButton = ({ className, onClick }: DeclineButtonProps) => {
  return (
    <button
      type="button"
      className="mt-1 flex size-5 cursor-pointer items-center justify-center transition hover:scale-125"
      onClick={onClick}
    >
      <X className={`h-5 w-5 ${className}`} />
    </button>
  );
};

export const DeclineButtonRed = ({ className, onClick }: DeclineButtonProps) => {
  return (
    <DeclineButton className={`text-coral hover:text-red-800 ${className}`} onClick={onClick} />
  );
};

export const DeclineButtonIvory = ({ className, onClick }: DeclineButtonProps) => {
  return (
    <DeclineButton className={`text-ivory hover:text-red-500 ${className}`} onClick={onClick} />
  );
};
