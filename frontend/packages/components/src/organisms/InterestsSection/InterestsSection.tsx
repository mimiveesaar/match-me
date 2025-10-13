"use client";

import { CharacterCounter } from "../../atoms/CharacterCounter/CharacterCounter";
import React, { useState, useEffect } from "react";

const COLORS = [
  "#F87171", // red-400
  "#FBBF24", // yellow-400
  "#34D399", // green-400
  "#60A5FA", // blue-400
  "#A78BFA", // purple-400
  "#F472B6", // pink-400
  "#FDBA74", // orange-400
  "#4ADE80", // emerald-400
];

const getColor = (str: string) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % COLORS.length;
  return COLORS[index];
};

interface Interest {
  id: number;
  name: string;
}

export const InterestsSection = ({
  selected,
  setSelected,
}: {
  selected: number[];
  setSelected: (items: number[]) => void;
}) => {
  const [interests, setInterests] = useState<Interest[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  // Fetch interests from backend
  useEffect(() => {
    const fetchInterests = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/interests");
        if (response.ok) {
          const data = await response.json();
          console.log("✓ Fetched interests from API:", data);
          setInterests(data);
        } else {
          console.error("Failed to fetch interests");
          useFallbackInterests();
        }
      } catch (error) {
        console.error("Error fetching interests:", error);
        useFallbackInterests();
      } finally {
        setIsLoading(false);
      }
    };

    const useFallbackInterests = () => {
      const fallbackInterests = [
        "Parallel parkour", "Cooking", "Stargazing", "Yoga", "Meditation",
        "Quantum Origami", "Starforging", "Telepathy Chess", "Black Hole Karaoke",
        "Baking", "Binary Poetry", "Painting", "Parallel Parking", "Alien Soap Opera",
        "Toes", "Collecting Rocks"
      ].map((name, index) => ({ id: index + 1, name }));
      setInterests(fallbackInterests);
    };

    fetchInterests();
  }, []);

  const toggleInterest = (interestId: number) => {
    if (selected.includes(interestId)) {
      setSelected(selected.filter((id) => id !== interestId));
    } else if (selected.length < 8) {
      setSelected([...selected, interestId]);
    }
  };

  const selectedInterests = selected
    .map((id) => interests.find((interest) => interest.id === id))
    .filter((i): i is Interest => i !== undefined);

  if (isLoading) {
    return (
      <div className="rounded border border-gray-300 p-6 flex items-center justify-center">
        <p>Loading interests...</p>
      </div>
    );
  }

  return (
    <div className="rounded-custom-16 border border-black/70 h-full p-6 flex flex-col lg:flex-row gap-6 lg:w-full">
      
      {/* Left side: Selected interests */}
      <div className="lg:w-1/2 w-full text-l flex flex-col">
        <span className="text-base italic mb-2">/my interests</span>

        <div className="w-full border border-gray-300 rounded-custom-8 p-3 mb-2 box-border">
          <div className="flex items-center justify-between mb-2">
            <p className="font-semibold text-gray-700">Pick up to 8</p>
            <CharacterCounter current={selected.length} max={8} />
          </div>

          {/* Big enough to comfortably show 8 interests */}
          <ul className="space-y-2 min-h-[10rem] max-h-none overflow-visible pr-2">

            {selectedInterests.map((interest) => (
              <li key={interest.id} className="text-sm flex items-center gap-2">
                <span
                  className="w-3 h-3 rounded-full inline-block"
                  style={{ backgroundColor: getColor(interest.name) }}
                />
                {interest.name}
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* Right side: All interests */}
      <div className="lg:w-1/2 w-full relative">

        {/* Scroll fade indicators */}
        <div className="absolute top-0 left-0 right-0 h-6 bg-gradient-to-b from-white to-transparent pointer-events-none z-10" />
        <div className="absolute bottom-0 left-0 right-0 h-6 bg-gradient-to-t from-white to-transparent pointer-events-none z-10" />

        <div className="flex flex-wrap gap-2 items-start content-start overflow-y-auto max-h-[40vh] p-1 scroll-smooth">
          {interests.map((interest) => {
            const isSelected = selected.includes(interest.id);
            return (
              <button
                key={interest.id}
                onClick={() => toggleInterest(interest.id)}
                className={`px-3 py-1 rounded shadow-sm text-sm transition-all duration-150
                  ${isSelected ? "bg-olive text-white" : "hover:bg-gray-100"}`}
              >
                {interest.name}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
};
