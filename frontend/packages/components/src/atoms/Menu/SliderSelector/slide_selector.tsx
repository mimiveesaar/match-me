"use client";

import React, { useState, useRef, useEffect } from "react";
import "./slide_selector.css";

interface RangeSliderProps {
  header?: string;
  min?: number;
  max?: number;
  step?: number;
  gap?: number;
}

export const RangeSlider: React.FC<RangeSliderProps> = ({
  header,
  min = 0,
  max = 1000,
  step = 1,
  gap = 50,
}) => {
  const [minVal, setMinVal] = useState(min);
  const [maxVal, setMaxVal] = useState(max);

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
          onChange={(e) =>
            setMinVal(Math.min(Number(e.target.value), maxVal - gap))
          }
        />

        <input
          type="range"
          min={min}
          max={max}
          step={step}
          value={maxVal}
          onChange={(e) =>
            setMaxVal(Math.max(Number(e.target.value), minVal + gap))
          }
        />

        <div className="slider " />
      </div>

      <div className="age-distance-input font-serif">
        <div className="field">
          <input
            type="number"
            value={minVal}
            min={min}
            max={maxVal - gap}
            className="text-left ml-0.5"
            readOnly
            onChange={(e) =>
              setMinVal(Math.min(Number(e.target.value), maxVal - gap))
            }
          />
        </div>
        <div className="field">
          <input
            type="number"
            value={maxVal}
            min={minVal + gap}
            max={max}
            className="text-right"
            readOnly
            onChange={(e) =>
              setMaxVal(Math.max(Number(e.target.value), minVal + gap))
            }
          />
        </div>
      </div>
    </div>
  );
};

