import type { Meta, StoryObj } from "@storybook/react";
import { CircleWrapper } from "./CircleWrapper";

const meta: Meta<typeof CircleWrapper> = {
  title: "Atoms/CircleWrapper",
  component: CircleWrapper,
  argTypes: {
    size: { control: "text" },
    className: { control: "text" },
    backgroundColor: { control: "text" },
    children: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof CircleWrapper>;

export const Default: Story = {
  args: {
    size: "w-32 h-32",
    backgroundColor: "bg-blue-200",
    className: "text-center text-sm",
    children: "I'm in a circle!",
  },
};

export const LargeRed: Story = {
  args: {
    size: "w-52 h-52",
    backgroundColor: "bg-red-300",
    children: "Big red circle",
  },
};

export const WithCustomContent: Story = {
  args: {
    size: "w-40 h-40",
    backgroundColor: "bg-green-200",
    children: <button className="px-4 py-2 bg-green-600 text-white rounded">Click Me</button>,
  },
};
