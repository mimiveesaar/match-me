"use client";

import { useEffect, useState } from "react";
import { MyProfilePage } from "../../../../../packages/components";

interface ProfileData {
  id: string;
  username: string;
  age: number;
  homeplanet: string;
  bodyform: string;
  lookingFor: string;
  homeplanetId?: number;
  bodyformId?: number;
  lookingForId?: number;
  bio: string;
  interests: string[]; // Interest names
  interestIds: number[]; // Interest IDs
  profilePic: string;
}

export default function MyProfile() {
  const [profile, setProfile] = useState<ProfileData | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/profiles/me');
      if (response.ok) {
        const data = await response.json();
        console.log("=== BACKEND RESPONSE ===", data);
        setProfile(data);
      } else {
        console.error('Failed to fetch profile');
        // If no profile exists, set a default structure
        setProfile({
          id: '',
          username: '',
          age: 0,
          homeplanet: '',
          bodyform: '',
          lookingFor: '',
          homeplanetId: 1,
          bodyformId: 1,
          lookingForId: 1,
          bio: '',
          interests: [],
          interestIds: [],
          profilePic: ''
        });
      }
    } catch (error) {
      console.error('Error fetching profile:', error);
      // Set default profile on error
      setProfile({
        id: '',
        username: '',
        age: 0,
        homeplanet: '',
        bodyform: '',
        lookingFor: '',
        homeplanetId: 1,
        bodyformId: 1,
        lookingForId: 1,
        bio: '',
        interests: [],
        interestIds: [],
        profilePic: ''
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleSaveProfile = async (updatedData: any) => {
    console.log("=== FRONTEND: Raw updatedData ===");
    console.log(JSON.stringify(updatedData, null, 2));

    try {
      const response = await fetch('http://localhost:8080/api/profiles/me', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
      });

      if (response.ok) {
        const savedProfile = await response.json();
        console.log('Profile updated successfully:', savedProfile);
        setProfile(savedProfile);
      } else {
        const errorText = await response.text();
        console.error('Failed to update profile:', errorText);
      }
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>;
  }

  return (
    <div>
      <MyProfilePage
        initialProfile={profile}
        onSave={handleSaveProfile}
      />
    </div>
  );
}