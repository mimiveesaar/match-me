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
            options={["Romance", "Friendship", "Strategic Alliance", "Co-parenting Hatchlings", "Host Symbiosis", "Chtulhu"]}
            selectedOptions={filters.lookingFor}
            onSelect={(val) => setFilters(f => ({ ...f, lookingFor: val }))}
            mode='single'
          />

          <DropdownSelector
            header="bodyform"
            options={["Gelatinous", "Tentacled", "Humanoid", "Energy-Based", "Mechanical", "Reptilian", "Gas Cloud", "Insectoid", "Crystalline", "Mimetic Blob"]}
            selectedOptions={filters.bodyform}
            onSelect={(val) => setFilters(f => ({ ...f, bodyform: val }))}
            mode='single'
          />

          <DropdownSelector
            header="interests"
            options={[
              "Telepathic Chess",
              "Black Hole Karaoke",
              "Baking",
              "Binary Poetry",
              "Painting",
              "Parallel Parking",
              "Reading",
              "Collecting Rocks",
              "Butterfly watching",
              "Plasma Sculpting",
              "Terraforming",
              "Zero-G Yoga",
              "Fishing",
              "Galactic Geocaching",
              "Nebula Photography",
              "Starship Racing",
              "Archaeology",
              "Cooking",
              "Light-speed Surfing",
              "Wormhole Navigation",
              "Cryo-sleep",
              "Martian Mining",
              "Solar Wind Sailing",
              "Meditation",
              "Opera Singing",
              "Ballet",
              "Fashion Design",
              "Black Market Trading",
              "Cosmic Comics",
              "Meteorite Hunting",
              "Exoplanet Exploration",
              "Star Map Reading",
              "Galactic Diplomacy",
              "Gardening",
              "Interstellar DJing",
              "Teleportation Tricks",
              "Brewing",
              "Droid Repair",
              "Cryptography",
              "Wormhole Jumping",
            ]}
            selectedOptions={filters.interests}
            onSelect={(val) => setFilters(f => ({ ...f, interests: val }))}
            mode='multiple'
          />

          <DropdownSelector
            header="location"
            options={[
              "Xeron-5",
              "Draknor",
              "Vega Prime",
              "Bloop-X12",
              "Zal'Tek Major",
              "Nimbus-9",
              "Krylon Beta",
              "Nova Eden",
              "Tharnis",
              "Quarnyx Delta",
              "Glooporia",
              "Skarn",
              "Uvuul-4",
              "Oortania",
              "Vrexalon",
            ]}
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