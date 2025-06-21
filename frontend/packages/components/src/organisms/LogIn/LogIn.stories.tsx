import type { Meta, StoryObj } from "@storybook/react";
import { LogIn } from "./LogIn";

const meta: Meta<typeof LogIn> = {
  title: "Organisms/LogIn",
  component: LogIn,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof LogIn>;

export const Default: Story = {};
