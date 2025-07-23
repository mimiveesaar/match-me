"use client";

import React, { ReactNode } from "react";
import { CircleCheckBig } from "lucide-react";

export const AcceptButton = () => {
  return (
    <button
      type="button"
      className="inline-flex cursor-pointer items-center justify-center transition-transform hover:scale-110"
      onClick={() => alert(`You two are now connected!`)}
    >
      <CircleCheckBig className="text-moss h-5 w-5 hover:text-green-800" />
    </button>
  );
};
