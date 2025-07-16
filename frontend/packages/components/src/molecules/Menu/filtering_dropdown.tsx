"use client";

import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React, { useState, useEffect } from "react";


export const FilteringDropdown = () => {

    const [lookingFor, setLookingFor] = useState<string[]>([]);
    const [bodyform, setBodyform] = useState<string[]>([]);
    const [interests, setInterests] = useState<string[]>([]);

    // for testing
    useEffect(() => {
    const filters = {
      lookingFor,
      bodyform,
      interests,
    };
    console.log("Filters updated:", filters);
    // Trigger fetch or filter update here
  }, [lookingFor, bodyform, interests]);


    return (
        <div className="relative">

            <RangeSlider
                header="age"
                min={18}
                max={1000}
                step={1}
                gap={5}

            />
            <RangeSlider
                header="distance"
                min={0}
                max={9300}
                step={10}
                gap={20}
            />

            <div className="pb-2">
                <div className="flex flex-col items-center">
                    <DropdownSelector
                        header="looking for"
                        options={["Intergalatctic Romance", "Frindship", "Travel Buddy"]}
                        selectedOptions={lookingFor}
                        onSelect={setLookingFor}
                        mode='single'
                    />
                    <DropdownSelector
                        header="bodyform"
                        options={["Gelatinous", "Tetrahedrous", "Dexaspherical", "Phospopede"]}
                        selectedOptions={bodyform}
                        onSelect={setBodyform}
                        mode='single'
                    />

                    <DropdownSelector
                        header="interests"
                        options={["Painting", "Binary poetry", "Helium inhalation"]}
                        selectedOptions={interests}
                        onSelect={setInterests}
                        mode='multiple'
                    />
                </div>
            </div>
        </div>
    );
}