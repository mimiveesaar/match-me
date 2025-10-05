"use client";

import { useEffect, useState } from "react";

import { MyProfilePage } from "components/organisms";


interface ProfileData {
  homeplanetId: number;
  bodyformId: number;
  lookingForId: number;
  bio: string;
  interestIds: number[];
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
        setProfile(data);
      } else {
        console.error('Failed to fetch profile');
      }
    } catch (error) {
      console.error('Error fetching profile:', error);
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

      console.log("=== FRONTEND: What we actually sent ===");
      console.log(JSON.stringify(updatedData, null, 2));

      if (response.ok) {
        console.log('Profile updated successfully');
        fetchProfile();
      } else {
        console.error('Failed to update profile');
      }
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>;
  }

  return (

    <div className="flex flex-col lg:flex-row gap-8">
      <div className="flex flex-col gap-4">

        <MyProfilePage
          initialProfile={profile}
          onSave={handleSaveProfile}
        />
      </div>
    </div>
  );
}