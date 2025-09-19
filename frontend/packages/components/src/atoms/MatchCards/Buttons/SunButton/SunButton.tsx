"use client";

import { Sun, Circle } from 'lucide-react';

export const SunButton = () => {
  return (
    <button
      type="button"
      className="no-flip group relative inline-flex items-center justify-center w-9 h-9 hover:scale-110 transition-transform cursor-pointer bg-transparent border-none p-0 appearance-none focus:outline-none"
      onClick={() => alert(`This user just got your full solar blessing ☀️!`)}
    >
      <Circle 
        color="#FFFCF7" 
        size={36} 
        strokeWidth={1} 
        className="absolute"
      />
      <Sun 
        color="#FFFCF7" 
        strokeWidth={2} 
        className="w-5 h-5 z-10 group-hover:stroke-[#FFFF0B]"
      />
    </button>
  );
}


