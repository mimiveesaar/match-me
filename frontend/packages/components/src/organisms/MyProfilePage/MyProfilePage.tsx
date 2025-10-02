"use client";

import React, { useState, useEffect } from "react";
import { ProfileCard } from "../ProfileCard/ProfileCard";
import { InterestsSection } from "../InterestsSection/InterestsSection";
import { MultiLineInputField } from "../../atoms/MultiLineInputField/MultiLineInputField";

interface MyProfilePageProps {
  initialProfile?: any;
  onSave?: (profileData: any) => void | Promise<void>;
}

export const MyProfilePage = ({
  initialProfile,
  onSave
}: MyProfilePageProps) => {
  const [bio, setBio] = useState(
    initialProfile?.bio || "Born among the glowing moons of Nebulon-5, I roam the stars in search of cosmic connection and shared stardust stories."
  );

  const [selectedInterests, setSelectedInterests] = useState<string[]>(
    initialProfile?.interestIds?.map(String) || []
  );

  const [profile, setProfile] = useState({
    name: "Xylar of Nebulon-5",
    age: "458",
    bodyform: "Spherioformes",
    lookingfor: "Romance",
    planet: "Nebulon-5",
    ...initialProfile
  });

  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (initialProfile) {
      setBio(initialProfile.bio || bio);
      setSelectedInterests(initialProfile.interestIds?.map(String) || []);
      setProfile(prev => ({ ...prev, ...initialProfile }));
    }
  }, [initialProfile]);

  const handleSave = async () => {
    const fullProfile = {
      homeplanetId: profile.homeplanetId || 1,
      bodyformId: profile.bodyformId || 1,
      lookingForId: profile.lookingForId || 1,
      bio,
      interestIds: selectedInterests.map(Number),
      profilePic: profile.profilePic || "https://example.com/default.jpg"
    };

    setIsLoading(true);

    try {
      if (onSave) {
        await onSave(fullProfile);
      } else {
        console.log("Saving profile:", fullProfile);
      }
    } catch (error) {
      console.error("Error saving profile:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex flex-col lg:flex-row gap-8 p-4 sm:p-6 lg:p-8 bg-neutral-50 min-h-screen">
      <div className="flex flex-col gap-4">
        <ProfileCard profile={profile} setProfile={setProfile} />
      </div>
      <div className="flex flex-col gap-4">
        <div className="bg-olive rounded p-6 shadow-md min-h-[12rem] flex flex-col justify-start">
          <span className="mb-2 text-xs italic">/bio</span>
          <MultiLineInputField
            placeholder="Bio (optional)"
            value={bio}
            onChange={(e) => setBio(e.target.value)}
            id="bio"
          />
        </div>

        <InterestsSection
          selected={selectedInterests}
          setSelected={setSelectedInterests}
        />

        <div className="flex items-center justify-center mt-4">
          <button
            onClick={handleSave}
            disabled={isLoading}
            className={`flex items-center gap-2 bg-olive hover:bg-amberglow text-white font-semibold py-2 px-4 rounded-xl shadow-md transition-all duration-200 ${isLoading ? 'opacity-50 cursor-not-allowed' : ''
              }`}
          >
            {isLoading ? 'Saving...' : 'Save Changes'}
          </button>
        </div>
      </div>
    </div>
  );
};