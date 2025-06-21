import type { Meta, StoryObj } from "@storybook/react";
import { AccountFormSignUp1 } from "./AccountFormSignUp1";

const meta: Meta<typeof AccountFormSignUp1> = {
  title: "Organisms/AccountFormSignUp1",
  component: AccountFormSignUp1,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof AccountFormSignUp1>;

export const Default: Story = {};
