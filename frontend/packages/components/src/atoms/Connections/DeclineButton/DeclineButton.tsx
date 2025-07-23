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
      className="inline-flex cursor-pointer items-center justify-center transition hover:scale-110"
      onClick={() => alert(``)}
    >
      <X className={`h-5 w-5 ${className}`} />
    </button>
  );
};

export const DeclineButtonRed = () => {
  return <DeclineButton className="text-coral hover:text-red-800"/>;
}

export const DeclineButtonIvory = () => {
  return <DeclineButton className="text-ivory hover:text-red-500" />;
};
