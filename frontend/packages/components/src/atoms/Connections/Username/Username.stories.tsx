import type { Meta, StoryObj } from "@storybook/nextjs";
import { ConnectionsUsername } from "./Username";

const meta: Meta<typeof ConnectionsUsername> = {
  title: "Atoms/Connections/Username",
  component: ConnectionsUsername,
  argTypes: {
    username: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof ConnectionsUsername>;

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
