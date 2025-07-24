"use client";

import { AcceptButton } from "@atoms/Connections/AcceptButton/AcceptButton";
import { DeclineButtonRed } from "@atoms/Connections/DeclineButton/DeclineButton";
import { Username } from "@atoms/Connections/Username/Username";
import { RoundProfilePicture } from "@atoms/ProfilePicture/ProfilePicture";
import React, { useState } from "react";

export const IncomingProfile = () => {
  return (
    <div className="flex w-full max-w-96 items-center justify-between px-10 py-2 md:max-w-72">
      <div className="flex items-center justify-center space-x-4">
        <RoundProfilePicture />
        <Username username="Marty" />
      </div>
      <div className="flex items-center justify-center space-x-2">
        <AcceptButton />
        <DeclineButtonRed />
      </div>
    </div>
  );
};
