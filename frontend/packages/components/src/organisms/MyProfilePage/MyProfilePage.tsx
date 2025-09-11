"use client";

import React, { useState, useEffect } from "react";
import { InterestsSection } from "../../organisms/InterestsSection/InterestsSection";
import { MultiLineInputField } from "../../atoms/MultiLineInputField/MultiLineInputField";
import { ProfileCard } from "../../organisms/ProfileCard/ProfileCard";
import { Menu } from "../../organisms/Menu/menu";

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

  useEffect(() => {
    if (initialProfile) {
      setBio(initialProfile.bio || bio);
      setSelectedInterests(initialProfile.interestIds?.map(String) || []);
      setProfile(prev => ({ ...prev, ...initialProfile }));
    }
  }, [initialProfile]);

  const handleSave = async () => {
    const fullProfile = {
      name: profile.name,
      age: profile.age,
      homeplanetId: profile.homeplanetId || 1,
      bodyformId: profile.bodyformId || 1,
      lookingForId: profile.lookingForId || 1,
      bio,
      interestIds: selectedInterests.map(Number),
      profilePic: profile.profilePic || "https://example.com/default.jpg"
    };

    if (onSave) {
      await onSave(fullProfile);
    } else {
      console.log("Saving profile:", fullProfile);
    }
  };

  return (
    <div className="flex">

      
      {/* Menu for mobile - will be handled by the hamburger button */}
      <div className="lg:hidden">
        <Menu />
      </div>

      {/* Main Content */}
      <div className="flex-1 lg:ml-0">
        <div className="flex flex-col lg:flex-row gap-8 p-4 sm:p-6 lg:p-8 bg-neutral-50 min-h-screen pt-16 lg:pt-8">
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
                id={"bio"}
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
      </div>
    </div>
  );
};