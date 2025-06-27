import type { Meta, StoryObj } from "@storybook/react";
import { CircleWrapper } from "./CircleWrapper";

const meta: Meta<typeof CircleWrapper> = {
  title: "Atoms/CircleWrapper",
  component: CircleWrapper,
  argTypes: {
    className: { control: "text" },
    children: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof CircleWrapper>;

export const Default: Story = {
  args: {

    className: "w-32 h-32 text-center text-sm bg-blue-200",
    children: "I'm in a circle!",
  },
};

export const LargeRed: Story = {
  args: {
    children: "Big red circle",
    className: "w-52 h-52 bg-red-300"
  },
};

export const WithCustomContent: Story = {
  args: {
    children: <button className="bg-green-200 w-40 h-40 px-4 py-2 bg-green-600 text-white rounded">Click Me</button>,
  },
};
