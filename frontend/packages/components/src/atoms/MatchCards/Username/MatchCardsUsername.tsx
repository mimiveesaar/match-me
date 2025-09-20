"use client";

interface UsernameProps {
  username: string;
}

export const MatchCardsUsername = ({ username }: UsernameProps) => {
  
  return (
    <span className="text-ivory text-2xl font-medium font-ibm_plex_sans drop-shadow-custom-2">
      {username}
    </span>
  );
}