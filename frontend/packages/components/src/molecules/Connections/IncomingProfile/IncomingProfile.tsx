"use client";

import React, { useState } from "react";
import { RoundProfilePicture, Username, AcceptButton, DeclineButtonRed } from "src/atoms";

export interface IncomingProfileProps {
  username?: string;
  profilePictureUrl?: string;
  onAccept?: () => void;
  onDecline?: () => void;
}

export const IncomingProfile = ({
  username,
  profilePictureUrl,
  onAccept,
  onDecline,
}: IncomingProfileProps) => {
  return (
    <div className="flex w-full max-w-96 items-center justify-between px-1 py-2">
      <div className="flex items-center justify-center space-x-4">
        <RoundProfilePicture imageUrl={profilePictureUrl} />
        <Username username={username} className="!text-black" />
      </div>
      <div className="flex items-center justify-center space-x-2">
        <AcceptButton onClick={onAccept} />
        <DeclineButtonRed onClick={onDecline} />
      </div>
    </div>
  );
};
