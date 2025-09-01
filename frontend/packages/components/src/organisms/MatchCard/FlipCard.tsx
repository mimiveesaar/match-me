"use client";

import React, { useState, ReactNode } from "react";
import "./flip_card.css";

interface FlipCardProps {
  front: ReactNode;
  back: ReactNode;
}

export const FlipCard = ({ front, back }: FlipCardProps) => {
  const [flipped, setFlipped] = useState(false);

  const handleFlip = (e: React.MouseEvent) => {
    if ((e.target as HTMLElement).closest(".no-flip")) return;
    setFlipped(prev => !prev);
  };

  return (
    <div className="flip-card relative w-full sm:w-[200px] md:w-[240px] lg:w-[280px] aspect-[265/360] min-h-[240px] perspective hover:scale-105 transition-transform duration-200 drop-shadow-custom-2" onClick={handleFlip}>
      <div className={`flip-card-inner ${flipped ? "flipped" : ""}`}>
        <div className="flip-card-front">{front}</div>
        <div className="flip-card-back">{back}</div>
      </div>
    </div>
  );
};