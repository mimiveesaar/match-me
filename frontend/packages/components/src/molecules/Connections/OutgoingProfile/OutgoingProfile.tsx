"use client";

import React, { useState } from "react";
import { RoundProfilePicture, Username, DeclineButtonIvory } from "@atoms";

export interface OutgoingProfileProps {
  username: string;
  profilePictureUrl: string;
  onDecline?: () => void;
}

export const OutgoingProfile = ({
  username,
  profilePictureUrl,
  onDecline,
}: OutgoingProfileProps) => {
  return (
    <div className="flex w-full items-center justify-between px-1 py-2">
      <div className="flex items-center justify-center space-x-4">
        <RoundProfilePicture imageUrl={profilePictureUrl} />
        <Username username={username} />
      </div>
      <DeclineButtonIvory onClick={onDecline} />
    </div>
  );
};
