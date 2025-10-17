"use client";

import { useEffect, useState } from "react";
import { MyProfilePage } from "./MyProfilePage";

interface ProfileData {
  id?: string;
  name?: string;
  age?: number;
  homeplanetId?: number;
  bodyformId?: number;
  lookingForId?: number;
  bio?: string;
  interestIds?: number[];
  profilePic?: string;
  error?: string;
}

interface SelectOption {
    value: string;
    label: string;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';


export default function MyProfile() {

    const [bodyformOptions, setBodyformOptions] = useState<SelectOption[]>([]);
    const [lookingforOptions, setLookingforOptions] = useState<SelectOption[]>([]);
    const [planetOptions, setPlanetOptions] = useState<SelectOption[]>([]);
    const [interestOptions, setInterestOptions] = useState<SelectOption[]>([]);
    const [optionsLoading, setOptionsLoading] = useState(true);
    const [errors, setErrors] = useState({
        nameError: "",
        ageError: "",
        bodyformError: "",
        lookingForError: "",
        planetError: "",
        interestsError: ""
    });

    useEffect(() => {
        const fetchOptions = async () => {
            try {
                setOptionsLoading(true);

                const [bodyformsRes, lookingForRes, planetsRes] = await Promise.all([
                    fetch(`${API_BASE_URL}/api/bodyforms`),
                    fetch(`${API_BASE_URL}/api/looking-for`),
                    fetch(`${API_BASE_URL}/api/homeplanets`),
                ]);

                const bodyforms = await bodyformsRes.json();
                const lookingFor = await lookingForRes.json();
                const planets = await planetsRes.json();

                setBodyformOptions(bodyforms.map((b: any) => ({ value: b.id, label: b.name })));
                setLookingforOptions(lookingFor.map((l: any) => ({ value: l.id, label: l.name })));
                setPlanetOptions(planets.map((p: any) => ({ value: p.id, label: p.name })));
            } catch (err) {
                console.error("❌ Error fetching options:", err);
            } finally {
                setOptionsLoading(false);
            }
        };

        fetchOptions();
    }, []);

  useEffect(() => {
    document.title = "My Profile | alien.meet";
  }, []);

  const [profile, setProfile] = useState<ProfileData | null>(null);
  const [isLoading, setIsLoading] = useState(true);


  useEffect(() => {
    // ✅ Token for testing
    localStorage.setItem(
      "jwtToken",
      "eyJhbGciOiJIUzI1NiIsImFhYSI6dHJ1ZX0.eyJpc3MiOiJtYXRjaC1tZSIsInVzZXJJZCI6IjAwMDAwMDAwLTAwMDAtMDAwMC0wMDAwLTAwMDAwMDAwMDAwMCIsIlVzZXJTdGF0dXNDb2RlIjoxLCJpYXQiOjE3NjA0Mjk1MDYsImV4cCI6Mjc2MDQzMDEwNn0.xSyC6faDJIyCDs3RtM7tRPutQz_7L73RY8Nn9ICa9qk"
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

    let hasErrors = false;

    setErrors({
      nameError: "",
      ageError: "",
      bodyformError: "",
      lookingForError: "",
      planetError: "",
      interestsError: ""
    });

      if (!updatedData.name) {
          setErrors(prev => ({ ...prev, nameError: "Name is required." }));
          hasErrors = true;
      }

      if (!updatedData.age) {
          setErrors(prev => ({ ...prev, ageError: "Age is required." }));
          hasErrors = true;
      }

      if (!updatedData.bodyformId) {
          setErrors(prev => ({ ...prev, bodyformError: "Please select a body form." }));
          hasErrors = true;
      }

      if (!updatedData.lookingForId) {
          setErrors(prev => ({ ...prev, lookingForError: "Please select who you’re looking for." }));
          hasErrors = true;
      }

      if (!updatedData.homeplanetId) {
          setErrors(prev => ({ ...prev, planetError: "Please select a home planet." }));
          hasErrors = true;
      }

      if (!updatedData.interestIds || updatedData.interestIds.length === 0) {
          setErrors(prev => ({ ...prev, interestsError: "Please select at least one interest." }));
          hasErrors = true;
      }

      if (hasErrors) {
          return;
      }

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
  const uploadFile = async (file: File) => {

    try {
      const formData = new FormData();
      formData.append("file", file);

      const token = localStorage.getItem("jwtToken"); // ✅ Grab token

      const response = await fetch(
        `${API_BASE_URL}/profiles/me/image`,

        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: formData,

        }
      );
    } catch (error) {
      console.error("❌ Error uploading image:", error);
      throw error;
    }
  };

  return (

    <div className="flex flex-col lg:flex-row gap-8 mt-2 lg:mt-24">
      <div className="flex flex-col gap-4">
        <MyProfilePage
          initialProfile={profile}
          onSave={handleSaveProfile}
          bodyformOptions={bodyformOptions}
          lookingforOptions={lookingforOptions}
          planetOptions={planetOptions}
          loading={optionsLoading}
          errors={errors}
        />
      </div>
    </div>
  );
}