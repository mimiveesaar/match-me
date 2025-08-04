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
    <div className="bg-moss flex h-44 w-full flex-col items-start space-y-2 rounded-3xl p-5 sm:w-64 md:w-80">
      <MyConnectionsHeader />
      <div className="flex w-full overflow-y-auto">
        <div className="flex w-full flex-col pr-1.5">
          {connections.map((connection) => (
            <ConnectionProfile
              key={connection.id}
              username={connection.username}
              profilePictureUrl={connection.profilePictureUrl}
              onDecline={() => {
                setConnections(
                  connections.filter((c) => c.id !== connection.id),
                );
              }}
            />
          ))}
        </div>
      </div>
    </div>
  );
};
