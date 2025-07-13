"use client";

import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React from "react";


export const FilteringDropdown = () => {
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
                        selectedOptions={[]}
                        onSelect={() => { }}
                    />
                    <DropdownSelector
                        header="bodyform"
                        options={["Gelatinous", "Tetrahedrous", "Dexaspherical", "Phospopede"]}
                        selectedOptions={[]}
                        onSelect={() => { }} />
                    <DropdownSelector
                        header="interests"
                        options={["Painting", "Binary poetry", "Helium inhalation"]}
                        selectedOptions={[]}
                        onSelect={() => { }} />
                </div>
            </div>
        </div>
    );
}