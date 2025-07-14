import type { Meta, StoryObj } from "@storybook/react";
import { UserSignUpForm } from "./UserSignUpForm";

const meta: Meta<typeof UserSignUpForm> = {
  title: "Organisms/UserSignUpForm",
  component: UserSignUpForm,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof UserSignUpForm>;

export const Default: Story = {};
