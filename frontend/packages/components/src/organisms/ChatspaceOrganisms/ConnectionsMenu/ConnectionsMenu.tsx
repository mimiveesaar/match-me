"use client";

import { ConnectionsMenuBackground, ConnectionsMenuHeader } from "@atoms";
import { PhotoBubbleWithUsername } from "@molecules";

interface UserConnection {
  id: string;
  username: string;
  src?: string;
  alt?: string;
  isOnline: boolean;
}

interface ConnectionsMenuProps {
  users: UserConnection[];
  onSelectUser: (userId: string) => void; // NEW
}

export const ConnectionsMenu = ({ users, onSelectUser }: ConnectionsMenuProps) => {
  return (
    <ConnectionsMenuBackground>
      <ConnectionsMenuHeader />
      {users.map((user) => (
        <PhotoBubbleWithUsername
          key={user.id}
          src={user.src}
          alt={user.alt}
          username={user.username}
          isOnline={user.isOnline}
          onClick={() => onSelectUser(user.id)} // forward the click
        />
      ))}
    </ConnectionsMenuBackground>
  );
};