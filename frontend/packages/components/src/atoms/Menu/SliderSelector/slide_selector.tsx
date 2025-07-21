"use client";

import React, { useState } from "react";
import "./slide_selector.css";

interface RangeSliderProps {
  header?: string;
  min?: number;
  max?: number;
  step?: number;
  gap?: number;
  selectedRange: [number, number];
  onChange: (range: [number, number]) => void;
}

export const RangeSlider: React.FC<RangeSliderProps> = ({
  header,
  min = 0,
  max = 1000,
  step = 1,
  gap = 50,
  selectedRange,
  onChange,
}) => {
  const [minVal, maxVal] = selectedRange;

  const handleMinChange = (val: number) => {
    const clamped = Math.min(val, maxVal - gap);
    onChange([clamped, maxVal]);
  };

  const handleMaxChange = (val: number) => {
    const clamped = Math.max(val, minVal + gap);
    onChange([minVal, clamped]);
  };

  return (
    <div className="range-wrapper">

      <header>
        <h2 className="font-serif text-sm">{header}</h2>
      </header>

      <div className="range-input mb-3">

        <input
          type="range"
          min={min}
          max={max}
          step={step}
          value={minVal}
          onChange={(e) => handleMinChange(Number(e.target.value))}
        />

        <input
          type="range"
          min={min}
          max={max}
          step={step}
          value={maxVal}
          onChange={(e) => handleMaxChange(Number(e.target.value))}
        />

        <div className="slider " />
      </div>

      <div className="age-distance-input font-serif">
        <div className="field">
          <input
            type="number"
            value={minVal}
            // min={min}
            // max={maxVal - gap}
            readOnly
            className="text-left ml-0.5"
          />
        </div>
        <div className="field">
          <input
            type="number"
            value={maxVal}
            // min={minVal + gap}
            // max={max}
            readOnly
            className="text-right"
          />
        </div>
      </div>
    </div>
  );
};


