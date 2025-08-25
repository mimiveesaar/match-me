"use client";

import { Filters } from "@/types";
import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React, { useEffect } from "react";


interface FilteringDropdownProps {
  filters: Filters;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
}

export const FilteringDropdown: React.FC<FilteringDropdownProps> = ({ filters, setFilters }) => {

  useEffect(() => {
    console.log("Filters updated:", filters);
  }, [filters]);

  return (
    <div className="relative">

      <RangeSlider
        header="age"
        min={18}
        max={150}
        step={1}
        gap={5}
        selectedRange={[filters.minAge, filters.maxAge]}
        onChange={(val) => setFilters(f => ({ ...f, minAge: val[0], maxAge: val[1] }))}
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

          <DropdownSelector
            header="location"
            options={["Mars", "Venus", "Juno"]}
            selectedOptions={filters.homeplanet}
            onSelect={(val) => setFilters(f => ({ ...f, homeplanet: val }))}
            mode='single'
          />

        </div>

        <RangeSlider
          header="max distance (light years)"
          min={0}
          max={150}
          step={10}
          gap={20}
          maxOnly={true}
          selectedRange={[0, filters.maxDistanceLy]}
          onChange={(val) => setFilters(f => ({ ...f, maxDistanceLy: val[1] }))}
        />
      </div>
    </div>
  );
};