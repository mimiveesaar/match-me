import type { Meta, StoryObj } from "@storybook/react";
import { NextPageIconButton } from "./NextPageIconButton";

const meta: Meta<typeof NextPageIconButton> = {
  title: "Atoms/NextPageIconButton",
  component: NextPageIconButton,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof NextPageIconButton>;

export const Default: Story = {
  args: {
    onClick: () => alert("Clicked!"),
  },
};
