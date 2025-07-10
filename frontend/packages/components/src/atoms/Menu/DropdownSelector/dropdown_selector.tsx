"use client";

import React, { useState } from "react";

export interface DropdownSelectorProps {
    header?: string;
    options?: string[];
    selectedOptions?: string[];
    onSelect?: (selected: string[]) => void;
}

export const DropdownSelector: React.FC<DropdownSelectorProps> = ({
    header,
    options,
    selectedOptions,
    onSelect,
}) => {

    const [isOpen, setIsOpen] = useState(false);

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    const handleOptionClick = (option: string) => {
        onSelect(option);
        // setIsOpen(false);
    };

    const buttonLabel =
        selectedOptions.length === 0
            ? '↴'
            : selectedOptions.length === 1
                ? selectedOptions[0]
                : `${selectedOptions.length} selected`;

    return (
        <div className="relative font-serif text-base">
            <div className="w-full text-center text-sm mb-1">
                {header}
            </div>
            <button
                className="w-[147px] h-[25px] text-left flex items-center bg-ivory p-2 rounded-lg drop-shadow-custom-2"
                onClick={toggleDropdown}
            >
                <span className="overflow-hidden whitespace-nowrap text-ellipsis block w-full">
                    {buttonLabel}
                </span>
            </button>
            {isOpen && (
                <ul className="absolute bg-ivory drop-shadow-custom-2 mt-1 w-full rounded-lg">
                    {options.map((option) => {
                        const isSelected = selectedOptions.includes(option);
                        return (
                            <li
                                key={option}
                                className={`p-2 cursor-pointer ${isSelected ? "bg-limeburst/40" : "hover:bg-limeburst/20"
                                    }`}
                                onClick={() => handleOptionClick(option)}
                            >
                                {option}
                            </li>
                        );
                    })}
                </ul>
            )}
        </div>
    );
};

// Example usage
// const options = ['Option 1', 'Option 2', 'Option 3'];
// const [selectedOption, setSelectedOption] = useState(options[0]);
// 
// <DropdownSelector
//   options={options}
//   selectedOption={selectedOption}
//   onSelect={setSelectedOption}
// />
// 
// This will render a dropdown selector with the provided options and handle selection changes.