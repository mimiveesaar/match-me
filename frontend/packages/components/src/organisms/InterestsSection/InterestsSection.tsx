"use client";

import { CharacterCounter } from "../../atoms";
import React, { useState, useEffect } from "react";

const COLORS = [
  "#DBDB72", // red-400
  "#FDC167", // yellow-400
  "#F6D8EC", // green-400
  "#D2F0EA", // blue-400
  "#BCC5AA", // purple-400
  "#EF764E", // pink-400
  "#30F84E", // orange-400
  "#F8D610", // emerald-400
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
  error,
}: {
  selected: number[];
  setSelected: (items: number[]) => void;
  error?: string;
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
        "Collecting Rocks",
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
      <div className="flex items-center justify-center rounded border border-gray-300 p-6">
        <p>Loading interests...</p>
      </div>
    );
  }

  return (
    <div className="rounded-custom-16 flex h-full flex-col gap-6 border border-black/70 p-6 lg:w-full lg:flex-row">
      {/* Left side: Selected interests */}
      <div className="text-l flex w-full flex-col lg:w-1/2">
        <span className="mb-2 text-base italic">/my interests</span>

        <div className="rounded-custom-8 mb-2 box-border w-full border border-gray-300 p-3">
          <div className="mb-2 flex items-center justify-between">
            <p className="font-semibold text-gray-700">Pick up to 8</p>
            <CharacterCounter current={selected.length} max={8} />
          </div>
            {error && (
                <div className="text-xs text-serif text-left text-black/70 mt-1">
                    {error}
                </div>
            )}

          {/* Big enough to comfortably show 8 interests */}
          <ul className="max-h-none min-h-[10rem] space-y-2 overflow-visible pr-2">
            {selectedInterests.map((interest) => (
              <li key={interest.id} className="flex items-center gap-2 text-sm">
                <span
                  className="inline-block h-3 w-3 rounded-full"
                  style={{ backgroundColor: getColor(interest.name) }}
                />
                {interest.name}
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* Right side: All interests */}
      <div className="relative w-full lg:w-1/2">

        <div className="pointer-events-none absolute top-0 right-0 left-0 z-10 h-6 bg-gradient-to-b from-white to-transparent" />
        <div className="pointer-events-none absolute right-0 bottom-0 left-0 z-10 h-6 bg-gradient-to-t from-white to-transparent" />

        <div className="flex max-h-[40vh] flex-wrap content-start items-start gap-2 overflow-y-auto scroll-smooth p-1">
          {interests.map((interest) => {
            const isSelected = selected.includes(interest.id);
            return (
              <button
                key={interest.id}
                onClick={() => toggleInterest(interest.id)}
                className={`rounded px-3 py-1 text-sm shadow-sm transition-all duration-150 ${isSelected ? "bg-olive text-white" : "hover:bg-gray-100"}`}
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
