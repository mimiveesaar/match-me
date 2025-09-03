import type { Meta, StoryObj } from "@storybook/nextjs";
import { Username } from '@atoms/CHATSPACE/Connections Menu/Username/Username';

const meta: Meta<typeof Username> = {
  title: "Atoms/ChatSpace/Connections Menu/Username",
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
  },
  parameters: {
    layout: "centered", 
  },
};