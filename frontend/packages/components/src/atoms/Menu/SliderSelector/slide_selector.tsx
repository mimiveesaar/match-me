"use client";

import React, { useState } from "react";

interface SlideSelectorProps {
  header: string;
  minimum: number;
  maximum: number;
  className?: string;
}

export const SlideSelector = ({
  header,
  minimum,
  maximum,
  className = "",
}: SlideSelectorProps) => {
  const [minValue, setMinValue] = useState(minimum);
  const [maxValue, setMaxValue] = useState(maximum);

  const handleMinChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = Math.min(Number(e.target.value), maxValue - 1);
    setMinValue(value);
  };

  const handleMaxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = Math.max(Number(e.target.value), minValue + 1);
    setMaxValue(value);
  };

  return (
    <div className={`w-full max-w-xs mx-auto ${className}`}>
      <div className="text-center text-xs mb-1">{header}</div>

      <div className="relative h-6">
        {/* Min slider */}
        <input
          type="range"
          min={minimum}
          max={maximum}
          value={minValue}
          onChange={handleMinChange}
          className="absolute w-full appearance-none cursor-pointer bg-transparent pointer-events-auto z-20 accent-black"
        />

        {/* Max slider */}
        <input
          type="range"
          min={minimum}
          max={maximum}
          value={maxValue}
          onChange={handleMaxChange}
          className="absolute w-full appearance-none cursor-pointer bg-transparent pointer-events-auto z-10 accent-black"
        />
      </div>

      <div className="flex justify-between text-xs mt-1">
        <span>{minValue}</span>
        <span>{maxValue}</span>
      </div>
    </div>
  );
};

