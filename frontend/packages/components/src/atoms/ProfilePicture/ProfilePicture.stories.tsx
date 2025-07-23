import type { Meta, StoryObj } from "@storybook/nextjs";
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

export const SmallCircle: Story = {
  args: {
    width: 50,
    height: 50,
    className: "rounded-full",
  },
};
