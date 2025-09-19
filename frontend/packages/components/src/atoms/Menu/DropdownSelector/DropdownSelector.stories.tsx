import React from "react";
import type { Meta, StoryObj } from "@storybook/nextjs";
import { DropdownSelector, DropdownSelectorProps } from "./DropdownSelector";

const meta: Meta<typeof DropdownSelector> = {
    title: "Atoms/Menu/DropdownSelector",
    component: DropdownSelector,
    argTypes: {
        header: { control: "text" },
        options: { control: "object" }, // safer than "array"
        selectedOptions: { control: "object" },
        onSelect: { action: "selected" },
    },
};
export default meta;

type Story = StoryObj<typeof DropdownSelector>;

export const Default: Story = {
    args: {
        header: "Choose option",
        options: ["Option 1", "Option 2", "Option 3"],
        selectedOptions: [],
        mode: "single",
    },
};

export const Interactive: Story = {
    render: (args: DropdownSelectorProps) => {
        const [selected, setSelected] = React.useState<string[]>(args.selectedOptions || []);

        const handleSelect = (option: string) => {
            setSelected((prev) =>
                prev.includes(option)
                    ? prev.filter((item) => item !== option)
                    : [...prev, option]
            );
        };

        return (
            <DropdownSelector
                {...args}
                selectedOptions={selected}

            />
        );
    },
    args: {
        header: "Looking for",
        options: [
            "Friendship",
            "Dating",
            "Networking",
            "Planetary playmate",
            "Intergalactic romance",
        ],
        selectedOptions: [],
    },
    parameters: {
        layout: "centered",
    },
};