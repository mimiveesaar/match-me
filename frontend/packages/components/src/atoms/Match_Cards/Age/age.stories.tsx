import type { Meta, StoryObj } from "@storybook/nextjs";
import { Age } from './age';

const meta: Meta<typeof Age> = {
  title: "Atoms/Match Cards/Age",
  component: Age,
  argTypes: {
    age: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof Age>;


export const Default: Story = {
  args: {
    age: "64",
  },
  parameters: {
    layout: "centered", 
  },
};