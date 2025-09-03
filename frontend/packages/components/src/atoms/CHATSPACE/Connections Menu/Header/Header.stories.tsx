import type { Meta, StoryObj } from "@storybook/nextjs";
import { Header } from "./Header";

const meta: Meta<typeof Header> = {
  title: "Atoms/ChatSpace/Connections Menu/Header",
  component: Header,
};

export default meta;

type Story = StoryObj<typeof Header>;


export const Default: Story = {
  parameters: {
    layout: "centered", 
  },
};