import type { Meta, StoryObj } from "@storybook/react";
import { AccountForm } from "./AccountForm";

const meta: Meta<typeof AccountForm> = {
  title: "Organisms/AccountForm",
  component: AccountForm,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof AccountForm>;

export const Default: Story = {};
