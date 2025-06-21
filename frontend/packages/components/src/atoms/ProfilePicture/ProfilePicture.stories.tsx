import type { Meta, StoryObj } from "@storybook/react";
import { ProfilePicture } from "./ProfilePicture";

const meta: Meta<typeof ProfilePicture> = {
  title: "Atoms/ProfilePicture",
  component: ProfilePicture,
  argTypes: {
    width: { control: "number" },
    height: { control: "number" },
    className: { control: "text" },
  },
};
export default meta;
type Story = StoryObj<typeof ProfilePicture>;

export const Default: Story = {
  args: {
    width: 260,
    height: 160,
  },
};
