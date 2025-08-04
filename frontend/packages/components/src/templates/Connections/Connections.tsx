import {
  IncomingPings,
  IncomingPingsProps,
} from "@organisms/Connections/IncomingPings/IncomingPings";
import {
  MyConnections,
  MyConnectionsProps,
} from "@organisms/Connections/MyConnections/MyConnections";
import {
  OutgoingPings,
  OutgoingPingsProps,
} from "@organisms/Connections/OutgoingPings/OutgoingPings";
import { Menu } from "@organisms/Menu/menu";
import React, { useState } from "react";

export interface ConnectionsProps {
  incomingPings: IncomingPingsProps;
  outgoingPings: OutgoingPingsProps;
  myConnections: MyConnectionsProps;
}

export const Connections = ({
  incomingPings,
  outgoingPings,
  myConnections,
}: ConnectionsProps) => {
  return (
    <div className="container mx-auto h-full">
      <div className="flex w-full flex-col justify-center sm:flex-row">
        <div className="collapse flex sm:visible">
          <Menu />
        </div>
        <div className="flex w-full flex-col space-y-3 sm:flex-row">
          <div className="flex justify-center">
            <MyConnections {...myConnections} />
          </div>
          <div className="flex flex-col items-center justify-center space-y-3">
            <IncomingPings {...incomingPings} />
            <OutgoingPings {...outgoingPings} />
          </div>
        </div>
      </div>
    </div>
  );
};
