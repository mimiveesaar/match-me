import React, { ReactNode } from "react";
import { Share2 } from "lucide-react";

export const MyConnectionsIcon = () => {
  return (
    <button type="button" className="inline-flex items-center justify-center">
      <Share2 className="text-ivory h-5 w-5" />
    </button>
  );
};
