"use client";

interface UsernameProps {
  username: string;
};

export const Username = ({ username }: UsernameProps) => {
  
  return (
    <span className="text-ivory text-sm font-medium chakra-petch">
      {username}
    </span>
  );
}