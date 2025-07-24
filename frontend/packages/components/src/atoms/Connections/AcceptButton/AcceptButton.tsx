"use client";

import React, { ReactNode } from "react";
import { CircleCheckBig } from "lucide-react";

export interface AcceptButtonProps {
  onClick?: () => void;
}

export const AcceptButton = ({ onClick }: AcceptButtonProps) => {
  return (
    <button
      type="button"
      className="mt-1 inline-flex size-4 cursor-pointer items-center justify-center transition-transform hover:scale-125"
      onClick={onClick}
    >
      <CircleCheckBig className="text-moss h-5 w-5 hover:text-green-800" />
    </button>
  );
};
