import type { Meta, StoryObj } from "@storybook/react";
import { Button } from "./button";

const meta: Meta<typeof Button> = {
  title: "Atoms/Button",
  component: Button,
  argTypes: {
    appName: { control: "text" },
    className: { control: "text" },
    children: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof Button>;

export const Default: Story = {
  args: {
    children: "Click Me",
    appName: "Demo",
  },
};

export const CustomClass: Story = {
  args: {
    children: "Styled Button",
    appName: "StyledApp",
    className: "bg-blue-500 text-white px-4 py-2 rounded",
  },
};

export const WithLongText: Story = {
  args: {
    children: "Click this button to see a custom alert from your app!",
    appName: "LongTextApp",
  },
};

