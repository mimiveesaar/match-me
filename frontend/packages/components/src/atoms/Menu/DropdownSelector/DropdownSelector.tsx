"use client";

import React from "react";

export interface DropdownSelectorProps {
    header?: string;
    options?: string[];
    selectedOptions: string[];
    onSelect: (newSelection: string[]) => void;
    mode?: "single" | "multiple";
}

export const DropdownSelector: React.FC<DropdownSelectorProps> = ({
    header,
    options = [],
    selectedOptions,
    onSelect,
    mode = "multiple",
}) => {
    const [isOpen, setIsOpen] = React.useState(false);

    const toggleDropdown = () => setIsOpen((prev) => !prev);

    const handleOptionClick = (option: string) => {
        let newSelection: string[];

        if (mode === "single") {
            if (selectedOptions.includes(option)) {
                newSelection = []; // Deselect if already selected
            } else {
                newSelection = [option]; // Select new one
                setIsOpen(false); // Optional: auto-close after selecting
            }
        } else {
            newSelection = selectedOptions.includes(option)
                ? selectedOptions.filter((item) => item !== option)
                : [...selectedOptions, option];
        }

        onSelect(newSelection);
    };

    const buttonLabel =
        selectedOptions.length === 0
            ? "↴"
            : selectedOptions.length === 1
                ? selectedOptions[0]
                : `${selectedOptions.length} selected`;

    return (
        <div className="relative font-serif text-base mb-6">
            {header && <div className="w-full text-center text-sm mb-1">{header}</div>}
            <button
                className="w-[147px] h-[25px] text-left flex items-center bg-ivory p-2 rounded-lg drop-shadow-custom-2"
                onClick={toggleDropdown}
            >
                <span className="overflow-hidden whitespace-nowrap text-ellipsis block w-full">
                    {buttonLabel}
                </span>
            </button>

            {isOpen && (
                <ul className="absolute z-10 bg-ivory drop-shadow-custom-2 mt-1 w-full rounded-lg max-h-32 overflow-y-auto">
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