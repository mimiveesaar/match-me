"use client";

import { DeclineButtonIvory } from "@atoms/Connections/DeclineButton/DeclineButton";
import { Username } from "@atoms/Connections/Username/Username";
import { RoundProfilePicture } from "@atoms/ProfilePicture/ProfilePicture";

export const ConnectionProfile = () => {
  return (
    <div className="flex w-full max-w-96 md:max-w-72 items-center justify-between px-10 py-2">
      <div className="flex items-center justify-center space-x-4">
        <RoundProfilePicture />
        <Username className="text-xl font-medium" username="Marty" />
      </div>
      <DeclineButtonIvory className="" />
    </div>
  );
};
