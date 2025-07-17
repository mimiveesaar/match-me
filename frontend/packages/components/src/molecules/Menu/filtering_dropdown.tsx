"use client";

import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React, { useEffect } from "react";

interface FilteringDropdownProps {
  filters: {
    lookingFor: string[];
    bodyform: string[];
    interests: string[];
  };
  setFilters: React.Dispatch<React.SetStateAction<{
    lookingFor: string[];
    bodyform: string[];
    interests: string[];
  }>>;
}

export const FilteringDropdown: React.FC<FilteringDropdownProps> = ({ filters, setFilters }) => {

  useEffect(() => {
    console.log("Filters updated:", filters);
    // Post request here if needed
  }, [filters]);

  return (
    <div className="relative">
      <RangeSlider header="age" min={18} max={1000} step={1} gap={5} />
      <RangeSlider header="distance" min={0} max={9300} step={10} gap={20} />

      <div className="pb-2">
        <div className="flex flex-col items-center">

          <DropdownSelector
            header="looking for"
            options={["Intergalactic Romance", "Frindship", "Travel Buddy"]}
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
            options={["Painting", "Binary poetry", "Helium inhalation"]}
            selectedOptions={filters.interests}
            onSelect={(val) => setFilters(f => ({ ...f, interests: val }))}
            mode='multiple'
          />

        </div>
      </div>
    </div>
  );
};