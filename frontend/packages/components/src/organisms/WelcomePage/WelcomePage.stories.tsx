import type { Meta, StoryObj } from "@storybook/react";
import { WelcomePage } from "./WelcomePage";

const meta: Meta<typeof WelcomePage> = {
  title: "Organisms/WelcomePage",
  component: WelcomePage,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof WelcomePage>;

export const Default: Story = {};
