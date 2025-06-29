"use client";

import React, { useState } from "react";
import { ProfileCard } from "@organisms/ProfoleCard/ProfileCard";
import { LabeledMultiLineInput } from "@molecules/LabeledMultiLineInput/LabeledMultiLineInput";
import { InterestsSection } from "@organisms/InterestsSection/InterestsSection";
import { CheckIcon } from "@atoms/CheckIcon/CheckIcon";

export const MyProfilePage = () => {
  const [bio, setBio] = useState(
    "Born among the glowing moons of Nebulon-5, I roam the stars in search of cosmic connection and shared stardust stories."
  );

  const [selectedInterests, setSelectedInterests] = useState<string[]>([]);
  const [profile, setProfile] = useState({
    name: "Xylar of Nebulon-5",
    age: "458",
    bodyform: "Spherioformes",
    lookingfor: "Romance",
    planet: "Nebulon-5",
  });

  const handleSave = () => {
    const fullProfile = {
      ...profile,
      bio,
      interests: selectedInterests,
    };
    console.log("Saving profile:", fullProfile);
    // You could POST this to a backend here
  };

  return (
    <div className="flex flex-col lg:flex-row gap-8 p-4 sm:p-6 lg:p-8 bg-neutral-50 min-h-screen">

      <div className="flex flex-col gap-4">
        <ProfileCard profile={profile} setProfile={setProfile} />
      </div>
      <div className="flex flex-col gap-4">

        <div className="bg-olive rounded p-6 shadow-md min-h-[12rem] flex flex-col justify-start">
          <span className="mb-2 text-xs italic">/bio</span>
          <LabeledMultiLineInput
            placeholder="Bio (optional)"
            value={bio}
            onChange={(e) => setBio(e.target.value)}
          />
        </div>

        <InterestsSection
          selected={selectedInterests}
          setSelected={setSelectedInterests}
        />

        <div className="flex items-center justify-center mt-4">
          <button
            onClick={handleSave}
            className="flex items-center gap-2 bg-olive hover:bg-amberglow text-white font-semibold py-2 px-4 rounded-xl shadow-md transition-all duration-200"
          >
            Save Changes
          </button>
        </div>

      </div>
    </div>
  );
};
