"use client";

import { useEffect, useState } from "react";
import { MyProfilePage } from "../../../../../packages/components";

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
    try {
      const response = await fetch('http://localhost:8080/api/profiles/me', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
      });

      if (response.ok) {
        console.log('Profile updated successfully');
        // Optionally refresh the profile data
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
    <div>
      <MyProfilePage 
        initialProfile={profile}
        onSave={handleSaveProfile}
      />
    </div>
  );
}