
import React, { useState } from "react";
import { MyConnectionsIcon } from "src/atoms";

export const MyConnectionsHeader = () => {
  return (
    <div className="flex items-center justify-center space-x-1">
      <h2 className="text-ivory">My Connections</h2>
      <MyConnectionsIcon />
    </div>
  );
};
