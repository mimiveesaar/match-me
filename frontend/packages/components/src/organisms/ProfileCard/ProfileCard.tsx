"use client";
import React from "react";
import { LabeledInputField } from "../../molecules";
import { LabeledSelectField } from "../../atoms";
import { ProfileImageUpload } from "../ProfileImageUpload/ProfileImageUpload";

interface SelectOption {
  value: string;
  label: string;
}

interface Profile {
  name?: string;
  age?: number | string;
  bodyformId?: string;
  lookingForId?: string;
  homeplanetId?: string;
  profilePic?: string;
}

type SetProfileFn = (profile: Profile) => void;

interface ProfileCardProps {
  profile: Profile;
  setProfile: SetProfileFn;
  onImageUpload: (file: File) => Promise<string>;
  bodyformOptions: SelectOption[];
  lookingforOptions: SelectOption[];
  planetOptions: SelectOption[];
  loading: boolean;
  errors?: {
    nameError?: string;
    ageError?: string;
    bodyformError?: string;
    lookingForError?: string;
    planetError?: string;
    interestsError?: string;
  };
}

export const ProfileCard = ({
  profile,
  setProfile,
  onImageUpload,
  bodyformOptions,
  lookingforOptions,
  planetOptions,
  loading,
  errors,
}: ProfileCardProps) => {
  const API_BASE_URL = "http://localhost:8080/api";

  const handleInputChange =
    (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
      const newValue = e.target.value;
      console.log(`ProfileCard: Updating ${field} to "${newValue}"`);
      setProfile({ ...profile, [field]: newValue });
    };

  const handleSelectChange = (field: string) => (value: string) => {
    setProfile({ ...profile, [field]: value });
  };

  if (loading) {
    return (
      <div className="bg-amberglow rounded-custom-16 drop-shadow-custom w-full p-6 lg:w-80">
        <div className="flex h-64 items-center justify-center">
          <p className="text-center text-lg">Loading options...</p>
        </div>
      </div>
    );
  }

  // Get values with fallbacks - ensure IDs are strings for select fields
  const displayName = profile?.name || "";
  const displayAge = String(profile?.age || "");
  const displayBodyform = profile?.bodyformId ? String(profile.bodyformId) : "";
  const displayLookingFor = profile?.lookingForId
    ? String(profile.lookingForId)
    : "";
  const displayPlanet = profile?.homeplanetId
    ? String(profile.homeplanetId)
    : "";

  console.log("ProfileCard: Current display values:", {
    name: displayName,
    age: displayAge,
    bodyformId: displayBodyform,
    lookingForId: displayLookingFor,
    homeplanetId: displayPlanet,
  });

  return (
    <div className="bg-amberglow rounded-custom-16 drop-shadow-custom w-full p-6 lg:w-80">
      <ProfileImageUpload
        currentImage={profile?.profilePic}
        onImageUpdate={(filename) => {
          setProfile({ ...profile, profilePic: filename });
        }}
        onImageUpload={onImageUpload}
      />

      <LabeledInputField
        label="name*"
        value={displayName}
        onChange={handleInputChange("name")}
        placeholder="Enter your name"
        error={errors?.nameError}
      />

      <LabeledInputField
        label="age*"
        value={displayAge}
        onChange={handleInputChange("age")}
        placeholder="Enter your age"
        error={errors?.ageError}
      />

      <LabeledSelectField
        id="bodyform"
        label="bodyform*"
        value={displayBodyform}
        onChange={(e) => handleSelectChange("bodyformId")(e.target.value)}
        options={bodyformOptions}
        setValue={handleSelectChange("bodyformId")}
        error={errors?.bodyformError}
      />

      <LabeledSelectField
        id="lookingfor"
        label="lookingfor*"
        value={displayLookingFor}
        onChange={(e) => handleSelectChange("lookingForId")(e.target.value)}
        options={lookingforOptions}
        setValue={handleSelectChange("lookingForId")}
        error={errors?.lookingForError}
      />

      <LabeledSelectField
        id="planet"
        label="planet*"
        value={displayPlanet}
        onChange={(e) => handleSelectChange("homeplanetId")(e.target.value)}
        options={planetOptions}
        setValue={handleSelectChange("homeplanetId")}
        error={errors?.planetError}
      />
    </div>
  );
};
