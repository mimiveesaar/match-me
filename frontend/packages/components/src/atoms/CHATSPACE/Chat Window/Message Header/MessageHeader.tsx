"use client";

import React from "react";

interface MessageHeaderProps {
  sender: string;
  time: string;      // e.g., "1:11 PM"
  date: string;      // e.g., "6/9/25"
}

export const MessageHeader = ({
  sender,
  time,
  date,
}: MessageHeaderProps) => {

  return (
    <div className={`flex justify-between items-center text-black/80 chakra-petch text-xs`}>
      <span>{sender}</span>
      <span>{time} {date}</span>
    </div>
  );
};