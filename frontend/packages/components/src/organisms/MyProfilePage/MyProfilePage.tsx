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
    initialProfile?.interestIds || []
  );

  const [profile, setProfile] = useState({
    name: initialProfile?.name || initialProfile?.username || "Xylar of Nebulon-5",
    age: initialProfile?.age || "458",
    bodyformId: initialProfile?.bodyformId || "",
    lookingForId: initialProfile?.lookingForId || "",
    homeplanetId: initialProfile?.homeplanetId || "",
    profilePic: initialProfile?.profilePic || "https://example.com/default.jpg"
  });

  const [isLoading, setIsLoading] = useState(false);

  // Update local state when initialProfile changes
  useEffect(() => {
    if (initialProfile) {
      console.log("MyProfilePage: Received initialProfile:", initialProfile);
      
      const newProfile = {
        name: initialProfile.name || initialProfile.username || "",
        age: String(initialProfile.age || ""),
        bodyformId: initialProfile.bodyformId || "",
        lookingForId: initialProfile.lookingForId || "",
        homeplanetId: initialProfile.homeplanetId || "",
        profilePic: initialProfile.profilePic || "https://example.com/default.jpg"
      };

      console.log("MyProfilePage: Setting profile to:", newProfile);
      setProfile(newProfile);
      setBio(initialProfile.bio || "");
      setSelectedInterests(initialProfile.interestIds || []);
    }
  }, [initialProfile?.id]); // Only re-run when the profile ID changes

  const handleSave = async () => {
    console.log("\n=== MyProfilePage: handleSave clicked ===");
    console.log("Current profile state:", profile);
    console.log("Current bio:", bio);
    console.log("Current selectedInterests:", selectedInterests);

    const fullProfile = {
      name: profile.name,
      age: profile.age ? Number(profile.age) : null,
      homeplanetId: profile.homeplanetId || null,
      bodyformId: profile.bodyformId || null,
      lookingForId: profile.lookingForId || null,
      bio: bio,
      interestIds: selectedInterests.filter(id => id != null),
      profilePic: profile.profilePic
    };

    console.log("Prepared profile data to save:", JSON.stringify(fullProfile, null, 2));

    setIsLoading(true);

    try {
      if (onSave) {
        console.log("Calling onSave handler...");
        await onSave(fullProfile);
        console.log("✓ onSave completed");
      } else {
        console.warn("⚠ No onSave handler provided");
      }
    } catch (error) {
      console.error("❌ Error in handleSave:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // Log whenever profile changes
  useEffect(() => {
    console.log("Profile state changed:", profile);
  }, [profile]);

  useEffect(() => {
    console.log("Bio changed:", bio);
  }, [bio]);

  useEffect(() => {
    console.log("Selected interests changed:", selectedInterests);
  }, [selectedInterests]);

  console.log("MyProfilePage render - current states:", {
    profile,
    bio: bio.substring(0, 50) + "...",
    interestCount: selectedInterests.length
  });

  return (
    <div className="flex flex-col lg:flex-row gap-5">

        <div className="flex flex-col gap-4 px-6">
        <ProfileCard profile={profile} setProfile={setProfile} />
        
        {/* Debug info - remove in production */}
        <div className="bg-gray-100 p-4 rounded text-xs">
          <div className="font-bold mb-2">Debug Info:</div>
          <div>Name: {profile.name}</div>
          <div>Age: {profile.age}</div>
          <div>BodyformId: {profile.bodyformId || "(empty)"}</div>
          <div>LookingForId: {profile.lookingForId || "(empty)"}</div>
          <div>HomeplanetId: {profile.homeplanetId || "(empty)"}</div>
          <div>Interests: {selectedInterests.length}</div>
        </div>
      </div>

        <div className="flex flex-col gap-4 w-full lg:w-[500px] px-6">
          <div className="bg-olive rounded-custom-16 p-6 drop-shadow-custom lg:min-h-[18rem] flex flex-col justify-start">
          <span className="mb-2 text-base italic text-ivory">/bio</span>
          <MultiLineInputField
            placeholder="Bio (optional)"
            value={bio}
            onChange={(e) => {
              console.log("Bio changing to:", e.target.value);
              setBio(e.target.value);
            }}
            id="bio"
          />
        </div>

        <InterestsSection
          selected={selectedInterests}
          setSelected={setSelectedInterests}
        />

        <div className="flex items-center justify-center mt-4 mb-4">
          <button
            onClick={() => {
              console.log("Save button clicked!");
              handleSave();
            }}
            disabled={isLoading}
            className={`flex items-center gap-2 bg-olive hover:bg-amberglow text-white font-semibold py-2 px-4 rounded-xl shadow-md transition-all duration-200 ${
              isLoading ? 'opacity-50 cursor-not-allowed' : ''
            }`}
          >
            {isLoading ? 'Saving...' : 'Save Changes'}
          </button>
        </div>
      </div>
    </div>
  );
};