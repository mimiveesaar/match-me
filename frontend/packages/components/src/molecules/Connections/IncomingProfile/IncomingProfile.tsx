"use client";

import { AcceptButton } from "@atoms/Connections/AcceptButton/AcceptButton";
import { DeclineButton } from "@atoms/Connections/DeclineButton/DeclineButton";
import { Username } from "@atoms/Connections/Username/Username";
import { ProfilePicture } from "@atoms/ProfilePicture/ProfilePicture";
import React, { useState } from "react";

export const IncomingProfile = () => {
  return (
    <div>
      <ProfilePicture />
      <Username username="Marty" />
      <AcceptButton />
      <DeclineButton />
    </div>
  );
};
