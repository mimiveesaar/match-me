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
    className={`rounded-2xl overflow-hidden bg-gray-200 ${className}`}
    style={{ width, height }}
  >
    <img
      src={imageUrl ?? "/default-profile.png"}
      alt="Profile"
      className="object-cover w-full h-full"
    />
  </div>
);
