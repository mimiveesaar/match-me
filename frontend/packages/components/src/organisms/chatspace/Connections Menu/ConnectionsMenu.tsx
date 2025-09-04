"use client";

import { PhotoBubbleWithUsername } from "@molecules/chatspace/connections-menu/photo-bubble-with-username/PhotoBubbleWithUsername";
import { Background } from "@atoms/chatspace/connections-menu/Background/Background";
import { Header } from "@atoms/chatspace/connections-menu/Header/Header";

interface ConnectionsMenuProps {
  src?: string;
  alt?: string;
  username: string;
}

export const ConnectionsMenu = ({
  src,
  alt,
  username,
}: ConnectionsMenuProps) => {
  return (
    <Background>
      <Header />
      <PhotoBubbleWithUsername src={src} alt={alt} username={username} />
      <PhotoBubbleWithUsername src={src} alt={alt} username={username} />
      <PhotoBubbleWithUsername src={src} alt={alt} username={username} />
    </Background>
  );
}