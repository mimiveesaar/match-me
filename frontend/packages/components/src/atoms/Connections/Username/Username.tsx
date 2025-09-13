import React, { ReactNode } from "react";

interface UsernameProps {
  username: string;
  className?: string;
}

export const Username = ({ username, className }: UsernameProps) => {
  return (
    <span
      className={`text-ivory max-w-40 sm:max-w-65 font-ibm-plex-sans break-words text-left text-wrap text-xl font-medium ${className}`}
    >
      {username}
    </span>
  );
};
