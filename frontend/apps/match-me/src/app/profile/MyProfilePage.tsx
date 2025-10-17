"use client";

import { InterestsSection, MultiLineInputField, ProfileCard } from "components";
import React, { useState, useEffect } from "react";

interface SelectOption {
    value: string;
    label: string;
}

interface MyProfilePageProps {
    initialProfile?: any;
    onSave?: (profileData: any) => void | Promise<void>;
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
    }
}

interface Profile {
    name?: string;
    age?: number | string;
    bodyformId?: string;
    lookingForId?: string;
    homeplanetId?: string;
    profilePic?: string;
}

export const MyProfilePage = ({
                                  initialProfile,
                                  onSave,
                                  bodyformOptions,
                                  lookingforOptions,
                                  planetOptions,
                                  loading,
    errors,
                              }: MyProfilePageProps) => {
    const [bio, setBio] = useState(initialProfile?.bio);
    const [selectedInterests, setSelectedInterests] = useState<number[]>(
        initialProfile?.interestIds || []
    );

    const [profile, setProfile] = useState<Profile>({
        name: initialProfile?.name,
        age: initialProfile?.age,
        bodyformId: initialProfile?.bodyformId,
        lookingForId: initialProfile?.lookingForId,
        homeplanetId: initialProfile?.homeplanetId,
        profilePic: initialProfile?.profilePic,
    });

    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (initialProfile) {
            console.log("MyProfilePage: Received initialProfile:", initialProfile);

            const newProfile = {
                name: initialProfile.name || "",
                age: String(initialProfile.age || ""),
                bodyformId: initialProfile.bodyformId || "",
                lookingForId: initialProfile.lookingForId || "",
                homeplanetId: initialProfile.homeplanetId || "",
                profilePic: initialProfile.profilePic || "https://example.com/default.jpg",
            };

            console.log("MyProfilePage: Setting profile to:", newProfile);
            setProfile(newProfile);
            setBio(initialProfile.bio || "");
            setSelectedInterests(initialProfile.interestIds || []);
        }
    }, [initialProfile?.id]);

    const handleSave = async () => {
        console.log("\n=== MyProfilePage: handleSave clicked ===");
        console.log("Current profile state:", profile);
        console.log("Current bio:", bio);
        console.log("Current selectedInterests:", selectedInterests);

        const toNumberOrNull = (value: any) => {
            if (!value || value === "") return null;
            const num = Number(value);
            return isNaN(num) ? null : num;
        };

        const fullProfile = {
            name: profile.name || "",
            age: toNumberOrNull(profile.age),
            homeplanetId: toNumberOrNull(profile.homeplanetId),
            bodyformId: toNumberOrNull(profile.bodyformId),
            lookingForId: toNumberOrNull(profile.lookingForId),
            bio: bio || "",
            interestIds: selectedInterests.filter(
                (id) => id != null && id !== undefined
            ),
            profilePic: profile.profilePic || "https://example.com/default.jpg",
        };

        setIsLoading(true);
        try {
            if (onSave) {
                await onSave(fullProfile);
                window.location.reload();
            }
        } catch (error) {
            console.error("❌ Error in handleSave:", error);
            alert("Error saving profile. Please try again.");
            setIsLoading(false);
        }
    };

    return (
        <div className="flex flex-col lg:flex-row gap-5">
            <div className="flex flex-col gap-4 px-6">
                <ProfileCard
                    onImageUpload={(file: File) => Promise.resolve("")}
                    profile={profile}
                    setProfile={setProfile}
                    bodyformOptions={bodyformOptions}
                    lookingforOptions={lookingforOptions}
                    planetOptions={planetOptions}
                    loading={loading}
                    errors={errors}
                />
            </div>

            <div className="flex flex-col gap-4 w-full lg:w-[500px] px-6">
                <div className="bg-olive rounded-custom-16 p-6 drop-shadow-custom lg:min-h-[18rem] flex flex-col justify-start">
                    <span className="mb-2 text-base italic text-ivory">/bio</span>
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
                    error={selectedInterests.length === 0 ? "Please select at least one interest" : undefined}
                />

                <div className="flex items-center justify-center mt-4 mb-4">
                    <button
                        onClick={handleSave}
                        disabled={isLoading}
                        className={`flex items-center gap-2 bg-olive hover:bg-amberglow text-white font-semibold py-2 px-4 rounded-xl shadow-md transition-all duration-200 ${
                            isLoading ? "opacity-50 cursor-not-allowed" : ""
                        }`}
                    >
                        {isLoading ? "Saving..." : "Save Changes"}
                    </button>
                </div>
            </div>
        </div>
    );
};
