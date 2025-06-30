"use client";

import React, { ReactNode } from "react";

interface UsernameProps {
  username: string;
};

export const Username = ({ username }: UsernameProps) => {
  
  return (
    <span className="text-ivory text-2xl font-medium font-ibm_plex_sans">
      {username}
    </span>
  );
}