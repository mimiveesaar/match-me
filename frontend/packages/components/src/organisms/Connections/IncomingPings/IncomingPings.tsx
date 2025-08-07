"use client";

import { IncomingPingsHeader } from "@molecules/Connections/IncomingPingsHeader/IncomingPingsHeader";
import { IncomingProfile } from "@molecules/Connections/IncomingProfile/IncomingProfile";
import { useState } from "react";

export interface IncomingPingsProps {
  incomingPings: IncomingPing[];
}

export interface IncomingPing {
  id: string;
  username: string;
  profilePictureUrl: string;
}

export const IncomingPings = ({ incomingPings }: IncomingPingsProps) => {
  const [pings, setPings] = useState<IncomingPing[]>(incomingPings);
  return (
    <div className="flex h-44 w-full flex-col items-start space-y-2 rounded-3xl border border-black/70 p-5 sm:h-56 sm:w-64 lg:w-96 lg:h-70">
      <IncomingPingsHeader />
      <div className="mt-1 flex w-full overflow-y-auto">
        <div className="flex w-full flex-col pr-1.5">
          {pings.map((ping) => (
            <IncomingProfile
              key={ping.id}
              username={ping.username}
              profilePictureUrl={ping.profilePictureUrl}
              onAccept={() => {
                setPings(pings.filter((p) => p.id !== ping.id));
              }}
              onDecline={() => {
                setPings(pings.filter((p) => p.id !== ping.id));
              }}
            />
          ))}
        </div>
      </div>
    </div>
  );
};
