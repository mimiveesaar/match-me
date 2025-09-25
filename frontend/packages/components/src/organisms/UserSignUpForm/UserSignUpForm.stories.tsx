import type { Meta, StoryObj } from "@storybook/nextjs";
import { UserSignUpForm } from "./UserSignUpForm";

const meta: Meta<typeof UserSignUpForm> = {
  title: "Organisms/UserSignUpForm",
  component: UserSignUpForm,
  tags: ["autodocs"],
};
export default meta;

type Story = StoryObj<typeof UserSignUpForm>;

import React, { useState } from "react";

export const Default: Story = {
  args: {
    errors: {
      emailError: '',
      passwordError: '',
      confirmPasswordError: '',
    },
  },
  argTypes: {
    errors: {
      control: 'object',
      description: 'Error messages for each field',
    },
    email: { control: 'text' },
    password: { control: 'text' },
    confirmPassword: { control: 'text' },
  },
  render: (args) => {
    const [email, setEmail] = useState(args.email || "");
    const [password, setPassword] = useState(args.password || "");
    const [confirmPassword, setConfirmPassword] = useState(args.confirmPassword || "");
    return (
      <UserSignUpForm
        email={email}
        setEmail={setEmail}
        password={password}
        setPassword={setPassword}
        confirmPassword={confirmPassword}
        setConfirmPassword={setConfirmPassword}
        errors={args.errors}
        {...args}
      />
    );
  },
};
