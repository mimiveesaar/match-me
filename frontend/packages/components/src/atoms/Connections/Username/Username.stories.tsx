import type { Meta, StoryObj } from "@storybook/nextjs";
import { Username } from "./Username";

const meta: Meta<typeof Username> = {
  title: "Atoms/Connections/Username",
  component: Username,
  argTypes: {
    username: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof Username>;

export const Default: Story = {
  args: {
    username: "Shelly",
    className: "text-sm",
  },
};

export const Large: Story = {
  args: {
    username: "Shelly",
    className: "text-4xl",
  },
};
