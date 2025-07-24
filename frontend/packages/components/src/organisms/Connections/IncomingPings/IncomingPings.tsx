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
    <div className="flex w-full max-w-92 flex-col items-start justify-center space-y-2 rounded-3xl border border-black/70 p-5 md:max-w-72">
      <IncomingPingsHeader />
      <div className="flex w-full flex-col">
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
  );
};
