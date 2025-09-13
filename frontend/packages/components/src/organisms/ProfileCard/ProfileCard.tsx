"use client";
import React, { useState, useEffect } from "react";
import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { LabeledSelectField } from "../../atoms/LabeledSelectField /LabeledSelectField";
import { ProfileCardData } from "@/types";

interface Option {
  value: string;
  label: string;
}

export const ProfileCard = ({
  profile,
  setProfile,
}: {
  profile: ProfileCardData;  // Use ProfileCardData here
  setProfile: (p: ProfileCardData) => void;  // And here
}) => {
  const [bodyformOptions, setBodyformOptions] = useState<Option[]>([]);
  const [lookingforOptions, setLookingforOptions] = useState<Option[]>([]);
  const [planetOptions, setPlanetOptions] = useState<Option[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchOptions = async () => {
      try {
        const [bodyformsRes, lookingForRes, homeplanetsRes] = await Promise.all([
          fetch('http://localhost:8080/api/bodyforms'),
          fetch('http://localhost:8080/api/looking-for'),
          fetch('http://localhost:8080/api/homeplanets')
        ]);

        const bodyforms = await bodyformsRes.json();
        const lookingFor = await lookingForRes.json();
        const homeplanets = await homeplanetsRes.json();

        setBodyformOptions(
          bodyforms.map((item: any) => ({
            value: item.id.toString(),
            label: item.name
          }))
        );

        setLookingforOptions(
          lookingFor.map((item: any) => ({
            value: item.id.toString(),
            label: item.name
          }))
        );

        setPlanetOptions(
          homeplanets.map((item: any) => ({
            value: item.id.toString(),
            label: item.name
          }))
        );

        setIsLoading(false);
      } catch (error) {
        console.error('Error fetching options:', error);
        setIsLoading(false);
      }
    };

    fetchOptions();
  }, []);

  // Debug effect to see current profile values
  useEffect(() => {
    console.log('ProfileCard - Current profile values:', {
      bodyform: profile.bodyform,
      bodyformId: profile.bodyformId,
      lookingfor: profile.lookingfor,
      lookingForId: profile.lookingForId,
      planet: profile.planet,
      homeplanetId: profile.homeplanetId
    });
  }, [profile]);

  const handleInputChange = (field: keyof ProfileCardData) => (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setProfile({ ...profile, [field]: e.target.value });
  };

  const handleSelectChange = (field: keyof ProfileCardData) => (
    e: React.ChangeEvent<HTMLSelectElement>
  ) => {
    const selectedId = parseInt(e.target.value);
    const selectedOption = getOptionByValue(field, e.target.value);
    
    // Create the correct ID field name mapping
    let idField: keyof ProfileCardData;
    if (field === 'bodyform') idField = 'bodyformId';
    else if (field === 'lookingfor') idField = 'lookingForId';
    else if (field === 'planet') idField = 'homeplanetId';
    else return; // Safety check
    
    setProfile({ 
      ...profile, 
      [field]: selectedOption?.label || e.target.value,
      [idField]: selectedId
    });
  };

  const getOptionByValue = (field: keyof ProfileCardData, value: string): Option | undefined => {
    switch (field) {
      case 'bodyform':
        return bodyformOptions.find(opt => opt.value === value);
      case 'lookingfor':
        return lookingforOptions.find(opt => opt.value === value);
      case 'planet':
        return planetOptions.find(opt => opt.value === value);
      default:
        return undefined;
    }
  };

  const getCurrentValue = (field: keyof ProfileCardData): string => {
    // Get the correct ID field for each field
    let currentId: number | undefined;
    if (field === 'bodyform') currentId = profile.bodyformId;
    else if (field === 'lookingfor') currentId = profile.lookingForId;
    else if (field === 'planet') currentId = profile.homeplanetId;
    
    return currentId ? currentId.toString() : '';
  };

  if (isLoading) {
    return (
      <div className="bg-amberglow rounded p-6 shadow-md w-80">
        <div className="animate-pulse">Loading profile options...</div>
      </div>
    );
  }

  return (
    <div className="bg-amberglow rounded p-6 shadow-md w-80">
      <img
        src="https://i.imgur.com/0y8Ftya.png"
        alt="alien"
        className="rounded mb-4 w-full object-cover h-40"
      />

      <LabeledInputField
        label="/name"
        value={profile.name}
        onChange={handleInputChange("name")}
        placeholder=""
      />
      <LabeledInputField
        label="/age"
        value={profile.age}
        onChange={handleInputChange("age")}
        placeholder=""
      />

      <LabeledSelectField
        id="bodyform"
        label="/bodyform"
        value={getCurrentValue("bodyform")}
        onChange={handleSelectChange("bodyform")}
        options={bodyformOptions}
      />

      <LabeledSelectField
        id="lookingfor"
        label="/lookingfor"
        value={getCurrentValue("lookingfor")}
        onChange={handleSelectChange("lookingfor")}
        options={lookingforOptions}
      />

      <LabeledSelectField
        id="planet"
        label="/planet"
        value={getCurrentValue("planet")}
        onChange={handleSelectChange("planet")}
        options={planetOptions}
      />
    </div>
  );
};