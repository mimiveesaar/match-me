"use client";

import React from "react";

interface ProfilePictureProps {
  width?: number;
  height?: number;
  className?: string;
  imageUrl?: string | null;
}

export const ProfilePicture: React.FC<ProfilePictureProps> = ({
  width = 260,
  height = 190,
  className = "",
  imageUrl = null,
}) => (
  <div
    className={`overflow-hidden rounded-2xl bg-gray-200 ${className}`}
    style={{ width, height }}
  >
    <img
      src={imageUrl ?? "/default-profile.png"}
      alt="Profile"
      className="h-full w-full object-cover"
    />
  </div>
);

export interface RoundProfilePictureProps {
  imageUrl?: string | null;
  className?: string;
}

export const RoundProfilePicture: React.FC<RoundProfilePictureProps> = ({
  imageUrl,
  className,
}) => (
  <ProfilePicture
    width={30}
    height={30}
    className={`rounded-full ${className}`}
    imageUrl={imageUrl}
  />
);
