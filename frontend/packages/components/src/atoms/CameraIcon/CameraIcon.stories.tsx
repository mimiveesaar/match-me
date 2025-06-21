import type { Meta, StoryObj } from "@storybook/react";
import { CameraIcon } from "./CameraIcon";

const meta: Meta<typeof CameraIcon> = {
  title: "Atoms/CameraIcon",
  component: CameraIcon,
  argTypes: {
    size: { control: "number" },
    className: { control: "text" },
  },
};
export default meta;
type Story = StoryObj<typeof CameraIcon>;

export const Default: Story = {
  args: {
    size: 24,
  },
};
