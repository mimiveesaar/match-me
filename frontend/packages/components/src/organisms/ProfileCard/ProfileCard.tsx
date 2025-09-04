"use client";
import React from "react";
import { LabeledInputField } from "@molecules/LabeledInputField/LabeledInputField";
import { LabeledSelectField } from "@atoms/LabeledSelectField /LabeledSelectField";

export const ProfileCard = ({
  profile,
  setProfile,
}: {
  profile: {
    name: string;
    age: string;
    bodyform: string;
    lookingfor: string;
    planet: string;
  };
  setProfile: (p: typeof profile) => void;
}) => {
  const handleInputChange = (field: keyof typeof profile) => (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setProfile({ ...profile, [field]: e.target.value });
  };

  const handleSelectChange = (field: keyof typeof profile) => (
    e: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setProfile({ ...profile, [field]: e.target.value });
  };

  const bodyformOptions = [
    { value: "humanoid", label: "Humanoid" },
    { value: "gelatinous", label: "Gelatinous" },
    { value: "tentacled", label: "Tentacled" },
    { value: "other", label: "Other" },
  ];

  const lookingforOptions = [
    { value: "friendship", label: "Friendship" },
    { value: "love", label: "Love" },
    { value: "adventure", label: "Adventure" },
  ];

  const planetOptions = [
    { value: "earth", label: "Earth" },
    { value: "mars", label: "Mars" },
    { value: "venus", label: "Venus" },
    { value: "kepler22b", label: "Kepler-22b" },
  ];

  return (
    <div className="bg-amberglow rounded p-6 shadow-md w-80">
      <img
        src="https://i.imgur.com/0y8Ftya.png"
        alt="alien"
        className="rounded mb-4 w-full object-cover h-40"
      />

      <LabeledInputField
        label="/name"
        value={profile.name}
        onChange={handleInputChange("name")}
        placeholder=""
      />
      <LabeledInputField
        label="/age"
        value={profile.age}
        onChange={handleInputChange("age")}
        placeholder=""
      />

      <LabeledSelectField
        id="bodyform"
        label="/bodyform"
        value={profile.bodyform}
        onChange={handleSelectChange("bodyform")}
        options={bodyformOptions}
      />

      <LabeledSelectField
        id="lookingfor"
        label="/lookingfor"
        value={profile.lookingfor}
        onChange={handleSelectChange("lookingfor")}
        options={lookingforOptions}
      />

      <LabeledSelectField
        id="planet"
        label="/planet"
        value={profile.planet}
        onChange={handleSelectChange("planet")}
        options={planetOptions}
      />
    </div>
  );
};
