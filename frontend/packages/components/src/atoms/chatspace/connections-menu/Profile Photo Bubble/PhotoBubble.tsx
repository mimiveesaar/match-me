"use client";

import React from "react";

interface PhotoBubbleProps {
  src?: string;
  alt?: string;
}

const DEFAULT_AVATAR = "default-avatar.png";

export const PhotoBubble = ({
  src,
  alt = "Profile picture",
}: PhotoBubbleProps) => {
  const handleError = (event: React.SyntheticEvent<HTMLImageElement, Event>) => {
    event.currentTarget.src = DEFAULT_AVATAR;
  };

  return (
    <div className={"w-12 h-12 rounded-full overflow-hidden"}>
      <img
        src={src || DEFAULT_AVATAR}
        alt={alt}
        onError={handleError}
        className="object-cover w-full h-full"
      />
    </div>
  );
};