import React, { ReactNode } from "react";
import { ArrowUpRight } from "lucide-react";

export const OutgoingPingIcon = () => {
  return (
    <button type="button" className="inline-flex items-center justify-center">
      <ArrowUpRight className="text-powder-blue h-5 w-5 mb-2" />
    </button>
  );
};
