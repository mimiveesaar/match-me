import { Meta, StoryObj } from "@storybook/nextjs";
import { FilteringDropdown } from "./FilteringDropdown";

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
    render: () => <FilteringDropdown filters={undefined} setFilters={function(value: any): void {
        throw new Error("Function not implemented.");
    } } />,
};