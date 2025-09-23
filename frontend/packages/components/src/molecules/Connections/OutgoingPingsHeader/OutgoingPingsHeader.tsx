
import React, { useState } from "react";
import { OutgoingPingIcon } from "@atoms";

export const OutgoingPingsHeader = () => {
  return (
    <div className="flex items-center justify-center space-x-0.5">
      <h2 className="text-powder-blue">Outgoing Pings</h2>
      <OutgoingPingIcon />
    </div>
  );
};
