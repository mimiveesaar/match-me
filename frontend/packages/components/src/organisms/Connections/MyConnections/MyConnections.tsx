"use client";

import { ConnectionProfile } from "@molecules/Connections/ConnectionProfile/ConnectionProfile";
import { IncomingPingsHeader } from "@molecules/Connections/IncomingPingsHeader/IncomingPingsHeader";
import { MyConnectionsHeader } from "@molecules/Connections/MyConnectionsHeader/MyConnectionsHeader";
import { useState } from "react";

export interface MyConnectionsProps {
  myConnections: MyConnection[];
}

export interface MyConnection {
  id: string;
  username: string;
  profilePictureUrl: string;
}

export const MyConnections = ({ myConnections }: MyConnectionsProps) => {
  const [connections, setConnections] = useState<MyConnection[]>(myConnections);
  return (
    <div className="flex w-full max-w-92 flex-col items-start justify-center space-y-2 rounded-3xl bg-moss p-5 md:max-w-72">
      <MyConnectionsHeader />
      <div className="flex w-full flex-col">
        {connections.map((connection) => (
          <ConnectionProfile
            key={connection.id}
            username={connection.username}
            profilePictureUrl={connection.profilePictureUrl}
            onDecline={() => {
              setConnections(connections.filter((c) => c.id !== connection.id));
            }}
          />
        ))}
      </div>
    </div>
  );
};
