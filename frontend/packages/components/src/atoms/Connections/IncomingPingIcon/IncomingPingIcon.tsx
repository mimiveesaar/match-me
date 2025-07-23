import React, { ReactNode } from "react";
import { ArrowDownLeft } from "lucide-react";

export const IncomingPingIcon = () => {
  return (
    <button type="button" className="inline-flex items-center justify-center">
      <ArrowDownLeft className="text-moss h-5 w-5" />
    </button>
  );
};
