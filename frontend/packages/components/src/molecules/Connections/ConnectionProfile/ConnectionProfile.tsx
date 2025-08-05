"use client";

import { DeclineButtonIvory } from "@atoms/Connections/DeclineButton/DeclineButton";
import { Username } from "@atoms/Connections/Username/Username";
import { RoundProfilePicture } from "@atoms/ProfilePicture/ProfilePicture";

export interface ConnectionProfileProps {
  username?: string;
  profilePictureUrl?: string;
  onDecline?: () => void;
}

export const ConnectionProfile = ({
  username,
  profilePictureUrl,
  onDecline,
}: ConnectionProfileProps) => {
  return (
      <div className="flex w-full max-w-96 items-center justify-between px-1 py-2">
        <button onClick={() => {}} className="flex items-center justify-center space-x-4 cursor-pointer">
          <RoundProfilePicture imageUrl={profilePictureUrl} />
          <Username username={username} />
        </button>
        <DeclineButtonIvory onClick={onDecline} />
      </div>
  );
};
