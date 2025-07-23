"use client";

import { Username } from "@atoms/Connections/Username/Username";
import {
  ProfilePicture,
  RoundProfilePicture,
} from "@atoms/ProfilePicture/ProfilePicture";
import React, { useState } from "react";

export const ConnectionProfile = () => {
  return (
    <div className="flex items-center justify-center space-x-2">
      <RoundProfilePicture />
      <Username className="text-2xl" username="Marty" />
    </div>
  );
};
