"use client";

import { DeclineButton } from "@atoms/Connections/DeclineButton/DeclineButton";
import { Username } from "@atoms/Connections/Username/Username";
import { ProfilePicture } from "@atoms/ProfilePicture/ProfilePicture";
import React, { useState } from "react";

export const OutgoingProfile = () => {
  return (
    <div>
      <ProfilePicture />
      <Username username="Marty" />
      <DeclineButton />
    </div>
  );
};
