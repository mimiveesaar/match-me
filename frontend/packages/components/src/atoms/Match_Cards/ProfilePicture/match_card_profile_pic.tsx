"use client";

import React from "react";

interface PicProps {
  src?: string;
  alt?: string;
  className?: string;
}

const DEFAULT_AVATAR = "default-avatar.png";

export const MatchCardProfilePic = ({
  src,
  alt = "Profile picture",
  className = "",
}: PicProps) => {
  const handleError = (event: React.SyntheticEvent<HTMLImageElement, Event>) => {
    event.currentTarget.src = DEFAULT_AVATAR;
  };

  return (
    <div className={`w-265 h-196 rounded-custom-16 overflow-hidden ${className}`}>
      <img
        src={src || DEFAULT_AVATAR}
        alt={alt}
        onError={handleError}
        className="object-cover w-full h-full"
      />
    </div>
  );
};