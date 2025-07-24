import { OutgoingPingIcon } from "@atoms/Connections/OutgoingPingIcon/OutgoingPingIcon";
import React, { useState } from "react";

export const OutgoingPingsHeader = () => {
  return (
    <div className="flex items-center justify-center space-x-0.5">
      <h2 className="text-powder-blue">Outgoing Pings</h2>
      <OutgoingPingIcon />
    </div>
  );
};
