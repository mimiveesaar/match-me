import React, { ReactNode } from "react";

interface UsernameProps {
  username: string;
  className?: string;
}

export const Username = ({ username, className }: UsernameProps) => {
  return (
    <span className={`text-ivory font-ibm_plex_sans font-medium ${className}`}>
      {username}
    </span>
  );
};
