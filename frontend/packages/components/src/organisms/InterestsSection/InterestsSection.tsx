"use client";

import { CharacterCounter } from "@atoms/CharacterCounter/CharacterCounter";
import React, { useState } from "react";

const ALL_INTERESTS = [
  "Parallel parkour",
  "Cooking",
  "Stargazing",
  "Yoga",
  "Meditation",
  "Quantum Origami",
  "Starforging",
  "Telepathy Chess",
  "Black Hole Karaoke",
  "Baking",
  "Binary Poetry",
  "Painting",
  "Parallel Parking",
  "Alien Soap Opera",
  "Toes",
  "Collecting Rocks"
];

export const InterestsSection = ({
  selected,
  setSelected,
}: {
  selected: string[];
  setSelected: (items: string[]) => void;
}) => {
  const toggleInterest = (interest: string) => {
    if (selected.includes(interest)) {
      setSelected(selected.filter((i) => i !== interest));
    } else if (selected.length < 8) {
      setSelected([...selected, interest]);
    }
  };

  return (
    <div className="rounded border border-gray-300 p-6 flex flex-col lg:flex-row gap-6 w-full">

      <div className="lg:w-1/2 w-full text-l">
        /my interests
        <div className="w-full border border-gray-300 rounded p-4">

          <p className="font-semibold text-gray-700 mb-2">Pick up to 8</p>
          <ul className="space-y-2">
            {selected.map((item) => (
              <li key={item} className="text-sm flex items-center gap-2">
                <span className="w-2 h-2 rounded-full bg-olive-600 inline-block" />
                {item}
              </li>
            ))}
            <CharacterCounter current={selected.length} max={8} />
          </ul>
        </div>
      </div>




      <div className="lg:w-1/2 w-full flex flex-wrap gap-2 items-start content-start">
        {ALL_INTERESTS.map((interest) => {
          const isSelected = selected.includes(interest);
          return (
            <button
              key={interest}
              onClick={() => toggleInterest(interest)}
              className={`px-3 py-1 rounded shadow-sm text-sm transition-all duration-150
                ${isSelected ? "bg-olive text-white" : " hover:bg-gray-100"}`}
            >
              {interest}
            </button>
          );
        })}
      </div>
    </div>
  );
};