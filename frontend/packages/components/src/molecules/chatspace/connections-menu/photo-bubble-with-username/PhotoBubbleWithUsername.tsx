"use client";

import { PhotoBubble } from "@atoms/chatspace/connections-menu/Profile Photo Bubble/PhotoBubble";
import { Username } from "@atoms/chatspace/connections-menu/Username/Username";

interface PhotoBubbleWithUsernameProps {
  src?: string;
  alt?: string;
  username: string;
}

export const PhotoBubbleWithUsername = ({
  src,
  alt,
  username,
}: PhotoBubbleWithUsernameProps) => {
  return (
    <div className="flex items-center justify-start gap-5 px-10 py-2">
      <PhotoBubble src={src} alt={alt} />
      <Username username={username} />
    </div>
  );
}