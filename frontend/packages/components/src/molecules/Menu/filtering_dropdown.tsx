"use client";

import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React, { useState } from "react";


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
            <div className="pt-4">
                <DropdownSelector
                    header="looking for"
                    options={["A", "B", "C"]}
                    selectedOptions={[]}
                    onSelect={() => { }}
                />
                <DropdownSelector
                    header="bodyform"
                    options={["A", "B", "C"]}
                    selectedOptions={[]}
                    onSelect={() => { }} />
                <DropdownSelector
                    header="interests"
                    options={["A", "B", "C"]}
                    selectedOptions={[]}
                    onSelect={() => { }} />
            </div>
        </div>
    );
}