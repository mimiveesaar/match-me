"use client";

interface UsernameProps {
  username: string;
}

export const Username = ({ username }: UsernameProps) => {
  
  return (
    <span className="text-ivory text-xl font-medium chakra-petch">
      {username}
    </span>
  );
}