"use client";
import React, { useEffect, useState } from "react";
import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { LabeledSelectField } from "../../atoms/LabeledSelectField/LabeledSelectField";
import { ProfileImageUpload } from "../ProfileImageUpload/ProfileImageUpload";

interface Option {
  id: string;
  name: string;
}

interface SelectOption {
  value: string;
  label: string;
}

interface Profile {
  username?: string;
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
}

export const ProfileCard = ({ profile, setProfile, onImageUpload }: ProfileCardProps) => {
  const [bodyformOptions, setBodyformOptions] = useState<SelectOption[]>([]);
  const [lookingforOptions, setLookingforOptions] = useState<SelectOption[]>([]);
  const [planetOptions, setPlanetOptions] = useState<SelectOption[]>([]);
  const [loading, setLoading] = useState(true);

  const API_BASE_URL = "http://localhost:8080/api";

  useEffect(() => {
    const fetchOptions = async () => {
      try {
        setLoading(true);

        const [bodyformsRes, lookingForRes, planetsRes] = await Promise.all([
          fetch(`${API_BASE_URL}/bodyforms`),
          fetch(`${API_BASE_URL}/looking-for`),
          fetch(`${API_BASE_URL}/homeplanets`),
        ]);

        const bodyforms: Option[] = await bodyformsRes.json();
        const lookingFor: Option[] = await lookingForRes.json();
        const planets: Option[] = await planetsRes.json();

        setBodyformOptions(bodyforms.map(item => ({ value: item.id, label: item.name })));
        setLookingforOptions(lookingFor.map(item => ({ value: item.id, label: item.name })));
        setPlanetOptions(planets.map(item => ({ value: item.id, label: item.name })));
      } catch (err) {
        console.error("Error fetching options:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchOptions();
  }, []);

  const handleInputChange = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    console.log(`ProfileCard: Updating ${field} to "${newValue}"`);
    setProfile({ ...profile, [field]: newValue });
  };

  const handleSelectChange = (field: string) => (value: string) => {
    setProfile({ ...profile, [field]: value });
  };

  if (loading) {
    return (
      <div className="bg-amberglow rounded-custom-16 p-6 drop-shadow-custom w-full lg:w-80">
        <div className="flex items-center justify-center h-64">
          <p className="text-center text-lg">Loading options...</p>
        </div>
      </div>
    );
  }

  // Get values with fallbacks - ensure IDs are strings for select fields
  const displayName = profile?.username || "";
  const displayAge = String(profile?.age || "");
  const displayBodyform = profile?.bodyformId ? String(profile.bodyformId) : "";
  const displayLookingFor = profile?.lookingForId ? String(profile.lookingForId) : "";
  const displayPlanet = profile?.homeplanetId ? String(profile.homeplanetId) : "";

  console.log("ProfileCard: Current display values:", {
    name: displayName,
    age: displayAge,
    bodyformId: displayBodyform,
    lookingForId: displayLookingFor,
    homeplanetId: displayPlanet,
  });

  return (
    <div className="bg-amberglow rounded-custom-16 p-6 drop-shadow-custom w-full lg:w-80">
      <ProfileImageUpload
        currentImage={profile?.profilePic}
        onImageUpdate={(filename) => {
          setProfile({ ...profile, profilePic: filename });
        }}
        onImageUpload={onImageUpload}
      />

      <LabeledInputField
        label="name"
        value={displayName}
        onChange={handleInputChange("name")}
        placeholder="Enter your username"
      />

      <LabeledInputField
        label="age"
        value={displayAge}
        onChange={handleInputChange("age")}
        placeholder="Enter your age"
      />

      <LabeledSelectField
        id="bodyform"
        label="/bodyform"
        value={displayBodyform}
        onChange={(e) => handleSelectChange("bodyformId")(e.target.value)}
        options={bodyformOptions}
        setValue={handleSelectChange("bodyformId")}
      />

      <LabeledSelectField
        id="lookingfor"
        label="/lookingfor"
        value={displayLookingFor}
        onChange={(e) => handleSelectChange("lookingForId")(e.target.value)}
        options={lookingforOptions}
        setValue={handleSelectChange("lookingForId")}
      />

      <LabeledSelectField
        id="planet"
        label="/planet"
        value={displayPlanet}
        onChange={(e) => handleSelectChange("homeplanetId")(e.target.value)}
        options={planetOptions}
        setValue={handleSelectChange("homeplanetId")}
      />
    </div>
  );
};