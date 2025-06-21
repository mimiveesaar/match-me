import type { Meta, StoryObj } from "@storybook/react";
import { SignUpOrLogInButton } from "./SignUpOrLogInButton";

const meta: Meta<typeof SignUpOrLogInButton> = {
  title: "Atoms/SignUpOrLogInButton",
  component: SignUpOrLogInButton,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof SignUpOrLogInButton>;

export const Default: Story = {
  args: {
    onClick: () => alert("Clicked!"),
  },
};
