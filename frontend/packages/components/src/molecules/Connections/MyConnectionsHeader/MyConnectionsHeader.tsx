import { MyConnectionsIcon } from "@atoms/Connections/MyConnectionsIcon/MyConnectionsIcon";
import React, { useState } from "react";

export const MyConnectionsHeader = () => {
  return (
    <div className="flex items-center justify-center space-x-1">
      <h2 className="text-ivory">My Connections</h2>
      <MyConnectionsIcon />
    </div>
  );
};
