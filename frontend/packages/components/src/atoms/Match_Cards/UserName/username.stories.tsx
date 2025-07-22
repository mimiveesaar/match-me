import type { Meta, StoryObj } from "@storybook/nextjs";
import { Username } from './username';

const meta: Meta<typeof Username> = {
  title: "Atoms/Match Cards/Usernme",
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