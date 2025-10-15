"use client";

import { useEffect, useState } from "react";
import { MyProfilePage } from "components/organisms";

interface ProfileData {
  id?: string;
  username?: string;
  age?: number;
  homeplanetId?: number;
  bodyformId?: number;
  lookingForId?: number;
  bio?: string;
  interestIds?: number[];
  profilePic?: string;
}

export default function MyProfile() {
  useEffect(() => {
    document.title = "My Profile | alien.meet";
  }, []);

  const [profile, setProfile] = useState<ProfileData | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // ✅ Token for testing
    localStorage.setItem(
      "jwtToken",
      "eyJhbGciOiJIUzI1NiIsImFhYSI6dHJ1ZX0.eyJpc3MiOiJtYXRjaC1tZSIsInVzZXJJZCI6IjExMTExMTExLTExMTEtMTExMS0xMTExLTExMTExMTExMTExMSIsIlVzZXJTdGF0dXNDb2RlIjoxLCJpYXQiOjE3NjA0Mjk1MDYsImV4cCI6Mjc2MDQzMDEwNn0.nmqTIiR1xiWCam1WhtcfXCRKCpuKdIxjyt1-5CY9G-Y"
    );

    fetchProfile();
  }, []);


  const fetchProfile = async () => {
    try {
      console.log("=== FETCHING PROFILE ===");

      const token = localStorage.getItem("jwtToken"); // ✅ Grab token
      const response = await fetch("http://localhost:8080/api/profiles/me", {
        headers: {
          Authorization: `Bearer ${token}`, // ✅ Send it to backend
        },
      });

      if (response.ok) {
        const data = await response.json();
        console.log("✓ Fetched profile from backend:", data);
        setProfile(data);
      } else {
        console.error("❌ Failed to fetch profile, status:", response.status);
      }
    } catch (error) {
      console.error("❌ Error fetching profile:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSaveProfile = async (updatedData: ProfileData) => {
    console.log("\n=== SAVING PROFILE ===");
    console.log("1. Raw data received from form:", updatedData);

    try {

      
      // Clean the data before sending
      const dataToSend = {
        age: updatedData.age ? Number(updatedData.age) : null,
        homeplanetId: updatedData.homeplanetId || null,
        bodyformId: updatedData.bodyformId || null,
        lookingForId: updatedData.lookingForId || null,
        bio: updatedData.bio || null,
        interestIds: (updatedData.interestIds || [])
          .filter(id => id != null)
          .map(id => Number(id)),
        profilePic: updatedData.profilePic || null,
      };

      console.log("2. Cleaned data to send:", JSON.stringify(dataToSend, null, 2));

      console.log("🚀 Final data being sent to backend:", JSON.stringify(dataToSend, null, 2));

      const token = localStorage.getItem("jwtToken"); // ✅ Grab token

      const response = await fetch('http://localhost:8080/api/profiles/me', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`, // ✅ Send it to backend
        },

        body: JSON.stringify(dataToSend),
      });

      console.log("3. Response status:", response.status);

      if (response.ok) {
        const result = await response.json();
        console.log("✓ Profile saved successfully!");
        console.log("4. Response from backend:", result);

        // Update local state with backend response
        setProfile(result);
      } else {
        const errorText = await response.text();
        console.error('❌ Failed to save profile');
        console.error('Error response:', errorText);
      }
    } catch (error) {
      console.error('❌ Error saving profile:', error);
    }
  };

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>;
  }

  return (

    <div className="flex flex-col lg:flex-row gap-8 mt-2 lg:mt-24">
      <div className="flex flex-col gap-4">
        <MyProfilePage
          initialProfile={profile}
          onSave={handleSaveProfile}
        />
      </div>
    </div>
  );
}