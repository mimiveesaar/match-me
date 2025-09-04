"use client";

import { PhotoBubbleWithUsername } from "@molecules/CHATSPACE/Connections Menu/PhotoBubbleWithUsername/PhotoBubbleWithUsername";
import { Background } from "@atoms/CHATSPACE/Connections Menu/Background/Background";
import { Header } from "@atoms/CHATSPACE/Connections Menu/Header/Header";

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