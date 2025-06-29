

"use client";
import React from "react";
import { LabeledInputField } from "@molecules/LabeledInputField/LabeledInputField";

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
  const handleChange = (field: keyof typeof profile) => (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setProfile({ ...profile, [field]: e.target.value });
  };

  return (
    <div className="bg-amberglow rounded p-6 shadow-md w-80">
      <img
        src="https://i.imgur.com/0y8Ftya.png"
        alt="alien"
        className="rounded mb-4 w-full object-cover h-40"
      />
      
      <LabeledInputField label="/name" value={profile.name} onChange={handleChange("name")} placeholder="" />
      <LabeledInputField label="/age" value={profile.age} onChange={handleChange("age")} placeholder="" />
      <LabeledInputField label="/bodyform" value={profile.bodyform} onChange={handleChange("bodyform")} placeholder="" />
      <LabeledInputField label="/lookingfor" value={profile.lookingfor} onChange={handleChange("lookingfor")} placeholder="" />
      <LabeledInputField label="/planet" value={profile.planet} onChange={handleChange("planet")} placeholder="" />
    </div>
  );
};
