"use client";

import React from "react";

interface PicProps {
  src?: string;
  alt?: string;
  className?: string;
}

const BACKEND_URL = "http://localhost:8080";
const DEFAULT_AVATAR = `${BACKEND_URL}/images/profiles/default-avatar.png`;

export const MatchCardProfilePic = ({ src, alt, className }: PicProps) => {
  
  const handleError = (event: React.SyntheticEvent<HTMLImageElement, Event>) => {
    event.currentTarget.src = DEFAULT_AVATAR;
  };

  const imageUrl = src 
    ? (src.startsWith('http') ? src : `${BACKEND_URL}${src}`)
    : DEFAULT_AVATAR;

  return (
    <div className={`w-265 h-196 rounded-custom-16 overflow-hidden ${className}`}>
      <img
        src={imageUrl}
        alt={alt}
        onError={handleError}
        className="object-cover w-full h-full"
      />
    </div>
  );
};