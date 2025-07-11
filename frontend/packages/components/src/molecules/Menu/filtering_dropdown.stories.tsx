import { Meta, StoryObj } from "@storybook/react";
import { FilteringDropdown } from "./filtering_dropdown";

const meta: Meta<typeof FilteringDropdown> = {
    title: "Molecules/Menu/Filtering Dropdown",
    component: FilteringDropdown,
    parameters: {
        layout: "centered",
    },
};
export default meta;

type Story = StoryObj<typeof FilteringDropdown>

export const Default: Story = {
    render: () => <FilteringDropdown />,
};