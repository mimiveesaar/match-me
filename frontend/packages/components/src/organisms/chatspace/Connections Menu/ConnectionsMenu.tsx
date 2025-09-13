"use client";

import { PhotoBubbleWithUsername } from "@molecules/chatspace/connections-menu/photo-bubble-with-username/PhotoBubbleWithUsername";
import { Background } from "@atoms/chatspace/connections-menu/Background/Background";
import { Header } from "@atoms/chatspace/connections-menu/Header/Header";

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
    <Background>
      <Header />
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
    </Background>
  );
};