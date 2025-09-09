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
  selected: string[]; // These should be interest IDs as strings
  setSelected: (items: string[]) => void;
}) => {
  const [interests, setInterests] = useState<Interest[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  // Fetch interests from backend
  useEffect(() => {
    const fetchInterests = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/interests');
        if (response.ok) {
          const data = await response.json();
          setInterests(data);
        } else {
          console.error('Failed to fetch interests');
          // Fallback to hardcoded interests if API fails
          const fallbackInterests = [
            "Parallel parkour", "Cooking", "Stargazing", "Yoga", "Meditation",
            "Quantum Origami", "Starforging", "Telepathy Chess", "Black Hole Karaoke",
            "Baking", "Binary Poetry", "Painting", "Parallel Parking", "Alien Soap Opera",
            "Toes", "Collecting Rocks"
          ].map((name, index) => ({ id: index + 1, name }));
          setInterests(fallbackInterests);
        }
      } catch (error) {
        console.error('Error fetching interests:', error);
        // Use fallback interests on error
        const fallbackInterests = [
          "Parallel parkour", "Cooking", "Stargazing", "Yoga", "Meditation",
          "Quantum Origami", "Starforging", "Telepathy Chess", "Black Hole Karaoke",
          "Baking", "Binary Poetry", "Painting", "Parallel Parking", "Alien Soap Opera",
          "Toes", "Collecting Rocks"
        ].map((name, index) => ({ id: index + 1, name }));
        setInterests(fallbackInterests);
      } finally {
        setIsLoading(false);
      }
    };

    fetchInterests();
  }, []);

  const toggleInterest = (interestId: number) => {
    const idString = interestId.toString();
    if (selected.includes(idString)) {
      setSelected(selected.filter((id) => id !== idString));
    } else if (selected.length < 8) {
      setSelected([...selected, idString]);
    }
  };

  // Get selected interests for display
  const getSelectedInterests = () => {
    return selected
      .map(id => interests.find(interest => interest.id.toString() === id))
      .filter(interest => interest !== undefined) as Interest[];
  };

  if (isLoading) {
    return (
      <div className="rounded border border-gray-300 p-6 flex items-center justify-center">
        <p>Loading interests...</p>
      </div>
    );
  }

  const selectedInterests = getSelectedInterests();

  return (
    <div className="rounded border border-gray-300 p-6 flex flex-col lg:flex-row gap-6 w-full">
      <div className="lg:w-1/2 w-full text-l">
        /my interests
        <div className="w-full border border-gray-300 rounded p-4">
          <p className="font-semibold text-gray-700 mb-2">Pick up to 8</p>
          
          <ul className="space-y-2">
            {selectedInterests.map((interest) => (
              <li key={interest.id} className="text-sm flex items-center gap-2">
                <span
                  className="w-3 h-3 rounded-full inline-block"
                  style={{ backgroundColor: getColor(interest.name) }}
                />
                {interest.name}
              </li>
            ))}
            <CharacterCounter current={selected.length} max={8} />
          </ul>
        </div>
      </div>

      <div className="lg:w-1/2 w-full flex flex-wrap gap-2 items-start content-start">
        {interests.map((interest) => {
          const isSelected = selected.includes(interest.id.toString());
          return (
            <button
              key={interest.id}
              onClick={() => toggleInterest(interest.id)}
              className={`px-3 py-1 rounded shadow-sm text-sm transition-all duration-150
                ${isSelected ? "bg-olive text-white" : " hover:bg-gray-100"}`}
            >
              {interest.name}
            </button>
          );
        })}
      </div>
    </div>
  );
};