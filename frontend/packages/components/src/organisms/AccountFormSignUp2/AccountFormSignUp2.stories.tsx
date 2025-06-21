import type { Meta, StoryObj } from "@storybook/react";
import { AccountFormSignUp2 } from "./AccountFormSignUp2";

const meta: Meta<typeof AccountFormSignUp2> = {
  title: "Organisms/AccountFormSignUp2",
  component: AccountFormSignUp2,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof AccountFormSignUp2>;

export const Default: Story = {};
