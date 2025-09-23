"use client";


import { useState } from "react";
import { OutgoingPingsHeader, OutgoingProfile } from "src/molecules";

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
    <div className="bg-peony flex h-44 w-full flex-col items-start space-y-2 rounded-3xl p-5 sm:h-56 sm:w-64 lg:w-96 lg:h-70">
      <OutgoingPingsHeader />
      <div className="mt-1 flex w-full overflow-y-auto">
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
