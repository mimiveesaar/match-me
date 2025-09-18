"use client";

import React, { ReactNode } from "react";

interface CardProps {
  children: ReactNode;
  className?: string;
  color?: "amberglow" | "olive" | "peony" | "minty" | "moss" | "coral";
}

export const MatchCardBackground = ({
  children,
  className = "",
  color = "minty",
}: CardProps) => {

const bgClassMap = {
    amberglow: "bg-amberglow",
    olive: "bg-olive",
    peony: "bg-peony",
    minty: "bg-minty",
    moss: "bg-moss",
    coral: "bg-coral",
  };

  const bgClass = bgClassMap[color] ?? bgClassMap.minty;

  return (
    <div className={`w-265 h-360 rounded-custom-16 drop-shadow-custom ${bgClass}`}>
      {children}
    </div>
  );
};
