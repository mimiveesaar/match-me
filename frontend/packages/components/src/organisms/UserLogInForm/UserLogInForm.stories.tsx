import type { Meta, StoryObj } from "@storybook/nextjs";
import { UserLoginForm } from "./UserLogInForm";

const meta: Meta<typeof UserLoginForm> = {
  title: "Organisms/UserLoginForm",
  component: UserLoginForm,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof UserLoginForm>;

export const Default: Story = {};
