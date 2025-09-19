"use client";

import { Background } from "@atoms/ChatspaceComponents/ConnectionsMenu/Background/Background";
import { Header } from "@atoms/ChatspaceComponents/ConnectionsMenu/Header/Header";
import { PhotoBubbleWithUsername } from "@molecules/ChatspaceMolecules/ConnectionsMenu/PhotoBubbleWithUsername/PhotoBubbleWithUsername";



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