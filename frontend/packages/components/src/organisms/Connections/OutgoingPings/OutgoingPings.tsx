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
    <div className="bg-peony flex h-44 w-full flex-col items-start space-y-2 rounded-3xl p-5 sm:w-64 md:w-80">
      <OutgoingPingsHeader />
      <div className="flex w-full overflow-y-auto">
        <div className="flex w-full flex-col pr-1.5">
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
    </div>
  );
};
