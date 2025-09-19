"use client";

import React from "react";
import "./SlideSelector.css";

interface RangeSliderProps {
  header?: string;
  min?: number;
  max?: number;
  step?: number;
  gap?: number;
  selectedRange: [number, number];
  onChange: (range: [number, number]) => void;
  maxOnly?: boolean;
}

export const RangeSlider: React.FC<RangeSliderProps> = ({
  header,
  min = 0,
  max = 1000,
  step = 1,
  gap = 50,
  selectedRange,
  onChange,
  maxOnly = false, 
}) => {
  const [minVal, maxVal] = selectedRange;

  const handleMinChange = (val: number) => {
    if (maxOnly) return; // Disable min changes if maxOnly mode
    const clamped = Math.min(val, maxVal - gap);
    onChange([clamped, maxVal]);
  };

  const handleMaxChange = (val: number) => {
    const clamped = maxOnly ? Math.max(val, min) : Math.max(val, minVal + gap);
    const newMin = maxOnly ? min : minVal;
    onChange([newMin, clamped]);
  };

  return (
    <div className="range-wrapper">
      <header>
        <h2 className="font-serif text-sm">{header}</h2>
      </header>

      <div className="range-input mb-3">
        {!maxOnly && (
          <input
            type="range"
            min={min}
            max={max}
            step={step}
            value={minVal}
            onChange={(e) => handleMinChange(Number(e.target.value))}
          />
        )}
        
        <input
          type="range"
          min={min}
          max={max}
          step={step}
          value={maxVal}
          onChange={(e) => handleMaxChange(Number(e.target.value))}
        />
        <div className="slider" />
      </div>

      <div className="age-distance-input font-serif">
        {!maxOnly && (
          <div className="field">
            <input
              type="number"
              value={minVal}
              readOnly
              className="text-left ml-0.5"
            />
          </div>
        )}
        
        <div className="field">
          <input
            type="number"
            value={maxVal}
            readOnly
            className={maxOnly ? "text-left ml-0.5" : "text-right"}
          />
        </div>
      </div>
    </div>
  );
};