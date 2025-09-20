import type { Meta, StoryObj } from "@storybook/nextjs";
import { ConnectionsMenuUsername } from "./ConnectionsMenuUsername";

const meta: Meta<typeof ConnectionsMenuUsername> = {
  title: "Atoms/ChatSpace/Connections Menu/Username",
  component: ConnectionsMenuUsername,
  argTypes: {
    username: { control: "text" },
  },
};

export default meta;

type Story = StoryObj<typeof ConnectionsMenuUsername>;

export const Default: Story = {
  args: {
    username: "Shelly",
  },
  parameters: {
    layout: "centered",
  },
};