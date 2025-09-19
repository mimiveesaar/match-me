import type { Meta, StoryObj } from "@storybook/nextjs";
import { LocationTag } from "./LocationTag";

const meta: Meta<typeof LocationTag> = {
  title: "Atoms/Match Cards/Location Tag",
  component: LocationTag,
  argTypes: {
    location: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof LocationTag>;

export const Default: Story = {
  args: {
    location: "Mars",
  },
  parameters: {
    layout: "centered", 
  },
};
