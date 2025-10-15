"use client";

import { InterestsSection, MultiLineInputField, ProfileCard } from "components";
import React, { useState, useEffect } from "react";


interface MyProfilePageProps {
  initialProfile?: any;
  onSave?: (profileData: any) => void | Promise<void>;
}

interface Profile{
  username?: string;
  age?: number | string;
  bodyformId?: string;
  lookingForId?: string;
  homeplanetId?: string;
  profilePic?: string;

}
export const MyProfilePage = ({
  initialProfile,
  onSave
}: MyProfilePageProps) => {
  const [bio, setBio] = useState(
    initialProfile?.bio || "Born among the glowing moons of Nebulon-5, I roam the stars in search of cosmic connection and shared stardust stories."
  );

  const [selectedInterests, setSelectedInterests] = useState<number[]>(
    initialProfile?.interestIds || []
  );

  const [profile, setProfile] = useState<Profile>({
    username: initialProfile?.username,
    age: initialProfile?.age,
    bodyformId: initialProfile?.bodyformId,
    lookingForId: initialProfile?.lookingForId,
    homeplanetId: initialProfile?.homeplanetId,
    profilePic: initialProfile?.profilePic,
  });

  const [isLoading, setIsLoading] = useState(false);

  // Update local state when initialProfile changes
  useEffect(() => {
    if (initialProfile) {
      console.log("MyProfilePage: Received initialProfile:", initialProfile);
      
      const newProfile = {
        username:initialProfile.username || "",
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

    // Helper function to convert to number or null
    const toNumberOrNull = (value: any) => {
      if (!value || value === "") return null;
      const num = Number(value);
      return isNaN(num) ? null : num;
    };

    // Ensure all fields are properly captured and converted to correct types
    const fullProfile = {
      username: profile.username || "",
      age: toNumberOrNull(profile.age),
      homeplanetId: toNumberOrNull(profile.homeplanetId),
      bodyformId: toNumberOrNull(profile.bodyformId),
      lookingForId: toNumberOrNull(profile.lookingForId),
      bio: bio || "",
      interestIds: selectedInterests.filter(id => id != null && id !== undefined),
      profilePic: profile.profilePic || "https://example.com/default.jpg"
    };

    console.log("Prepared profile data to save:", JSON.stringify(fullProfile, null, 2));
    console.log("All field values:", {
      username: fullProfile.username,
      age: fullProfile.age,
      homeplanetId: fullProfile.homeplanetId,
      bodyformId: fullProfile.bodyformId,
      lookingForId: fullProfile.lookingForId,
      bioLength: fullProfile.bio.length,
      interestCount: fullProfile.interestIds.length
    });

    setIsLoading(true);

    try {
      if (onSave) {
        console.log("Calling onSave handler...");
        await onSave(fullProfile);
        console.log("✓ onSave completed");
        
        // Force page refresh after successful save
        window.location.reload();
      } else {
        console.warn("⚠ No onSave handler provided");
        setIsLoading(false);
      }
    } catch (error) {
      console.error("❌ Error in handleSave:", error);
      alert('Error saving profile. Please try again.');
      setIsLoading(false);
    }
    // No finally block needed since page will reload on success
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
        <ProfileCard onImageUpload={(file) => { 
          console.log("onImageUpload called with file:", file);
          return Promise.resolve("") }} 
        profile={profile} setProfile={setProfile} />
        
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