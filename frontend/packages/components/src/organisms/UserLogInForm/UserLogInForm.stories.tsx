import type { Meta, StoryObj } from "@storybook/nextjs";
import { UserLoginForm } from "./UserLogInForm";
import React, { useState } from "react";

const meta: Meta<typeof UserLoginForm> = {
  title: "Organisms/UserLoginForm",
  component: UserLoginForm,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof UserLoginForm>;
export const Default: Story = {
  args: {
    errors: {
      emailError: '',
      passwordError: '',
    },
  },
  argTypes: {
    errors: {
      control: 'object',
      description: 'Error messages for each field',
    },
    email: { control: 'text' },
    password: { control: 'text' },
  },
  render: (args) => {
    const [email, setEmail] = useState(args.email || "");
    const [password, setPassword] = useState(args.password || "");
    return (
      <UserLoginForm
        email={email}
        setEmail={setEmail}
        password={password}
        setPassword={setPassword}
        errors={args.errors}
        {...args}
      />
    );
  },
};
