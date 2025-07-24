"use client";

import { OutgoingPingsHeader } from "@molecules/Connections/OutgoingPingsHeader/OutgoingPingsHeader";
import { OutgoingProfile } from "@molecules/Connections/OutgoingProfile/OutgoingProfile";
import { useState } from "react";

export interface OutgoingPingsProps {
  outgoingPings: OutgoingPing[];
}

export interface OutgoingPing {
  id: string;
  username: string;
  profilePictureUrl: string;
}

export const OutgoingPings = ({ outgoingPings }: OutgoingPingsProps) => {
  const [pings, setPings] = useState<OutgoingPing[]>(outgoingPings);
  return (
    <div className="bg-peony flex w-full max-w-92 flex-col items-start justify-center space-y-2 rounded-3xl p-5 md:max-w-72">
      <OutgoingPingsHeader />
      <div className="flex w-full flex-col">
        {pings.map((ping) => (
          <OutgoingProfile
            key={ping.id}
            username={ping.username}
            profilePictureUrl={ping.profilePictureUrl}
            onDecline={() => {
              setPings(pings.filter((p) => p.id !== ping.id));
            }}
          />
        ))}
      </div>
    </div>
  );
};
