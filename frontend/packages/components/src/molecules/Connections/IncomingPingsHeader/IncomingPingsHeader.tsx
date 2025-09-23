
import React, { useState } from "react";
import { IncomingPingIcon } from "src/atoms";

export const IncomingPingsHeader = () => {
  return (
    <div className="flex items-center justify-center space-x-0.5">
      <h2 className="text-moss">Incoming Pings</h2>
      <IncomingPingIcon />
    </div>
  );
};
