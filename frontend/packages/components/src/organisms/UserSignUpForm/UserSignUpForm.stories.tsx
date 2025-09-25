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
  render: (args) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    return (
      <UserSignUpForm
        email={email}
        setEmail={setEmail}
        password={password}
        setPassword={setPassword}
        confirmPassword={confirmPassword}
        setConfirmPassword={setConfirmPassword}
        {...args}
      />
    );
  },
};
