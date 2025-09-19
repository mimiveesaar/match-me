import type { Meta, StoryObj } from "@storybook/nextjs";
import { MatchCardBackground } from "./MatchCardBackground";

const meta: Meta<typeof MatchCardBackground> = {
  title: "Atoms/Match Cards/MatchCardBackground",
  component: MatchCardBackground,
  argTypes: {
    className: { control: "text" },
    children: { control: "text" },
  },
};
export default meta;

type Story = StoryObj<typeof MatchCardBackground>;

export const Default: Story = {
  args: {
    className: "flex justify-center items-center text-center text-sm bg-olive",
    children: "Profile information goes here",
  },
  parameters: {
    layout: "centered", 
  },
};