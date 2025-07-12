"use client";

import { DropdownSelector } from "@atoms/Menu/DropdownSelector/dropdown_selector";
import { RangeSlider } from "@atoms/Menu/SliderSelector/slide_selector";
import React, { useState } from "react";


export const FilteringDropdown = () => {
    return (
        <div className="relative">
            <RangeSlider 
                header="age"
            />
            <RangeSlider 
                header="distance"
            />

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
    );
}