"use client";

import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React, { useEffect } from "react";

interface FilteringDropdownProps {
  filters: {
    lookingFor: string[];
    bodyform: string[];
    interests: string[];
    ageRange: [number, number];
    distanceRange: [number, number];
  };
  setFilters: React.Dispatch<React.SetStateAction<{
    lookingFor: string[];
    bodyform: string[];
    interests: string[];
    ageRange: [number, number];
  }>>;
}

export const FilteringDropdown: React.FC<FilteringDropdownProps> = ({ filters, setFilters }) => {

  useEffect(() => {
    console.log("Filters updated:", filters);
    // Post request here if needed
  }, [filters]);

  return (
    <div className="relative">

      <RangeSlider
        header="age"
        min={18}
        max={150}
        step={1}
        gap={5}
        selectedRange={filters.ageRange}
        onChange={(val) => setFilters(f => ({ ...f, ageRange: val }))}
      />

      <RangeSlider
        header="distance"
        min={0} max={9300}
        step={10} gap={20}
        selectedRange={filters.distanceRange}
        onChange={(val) => setFilters(f => ({ ...f, distanceRange: val }))}
      />

      <div className="pb-2">
        <div className="flex flex-col items-center">

          <DropdownSelector
            header="looking for"
            options={["Intergalactic Romance", "Friendship", "Travel Buddy"]}
            selectedOptions={filters.lookingFor}
            onSelect={(val) => setFilters(f => ({ ...f, lookingFor: val }))}
            mode='single'
          />

          <DropdownSelector
            header="bodyform"
            options={["Gelatinous", "Vaporous", "Dexaspherical", "Phospopede"]}
            selectedOptions={filters.bodyform}
            onSelect={(val) => setFilters(f => ({ ...f, bodyform: val }))}
            mode='single'
          />

          <DropdownSelector
            header="interests"
            options={["hiking", "binary poetry", "yoga", "foraging mushrooms", "movies", "cooking", "counting sheep"]}
            selectedOptions={filters.interests}
            onSelect={(val) => setFilters(f => ({ ...f, interests: val }))}
            mode='multiple'
          />

        </div>
      </div>
    </div>
  );
};