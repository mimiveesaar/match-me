"use client";

import { LabeledInputField } from "@molecules/LabeledInputField/LabeledInputField";
import { NextPageIconButton } from "@atoms/NextPageIconButton/NextPageIconButton";
import { CircleWrapper } from "@atoms/CircleWrapper/CircleWrapper";


export interface UserSignUpFormProps {
  email: string;
  setEmail: (email: string) => void;
  password: string;
  setPassword: (password: string) => void;
  confirmPassword: string;
  setConfirmPassword: (confirmPassword: string) => void;
  onSubmit?: () => void;
}

export const UserSignUpForm = ({ email, password, confirmPassword, setEmail, setPassword, setConfirmPassword, onSubmit }: UserSignUpFormProps) => {

  return (
    <CircleWrapper className="bg-minty max-md:rounded-none rounded-2xl md:w-xl xl:w-xl aspect-square text-center">
      <div className="flex flex-col items-center py-10 md:p-20 ">
        <h2 className="text-2xl mb-5 font-semibold md:mt-2 md:text-3xl">
          New Account
        </h2>
        <LabeledInputField
          label="Email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <LabeledInputField
          label="Password"
          placeholder="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <LabeledInputField
          label="Password"
          placeholder="Confirm Password"
          value={confirmPassword}
          type="password"
          onChange={(e) => setConfirmPassword(e.target.value)}
        />

        <NextPageIconButton onClick={() => onSubmit && onSubmit()} />
      </div>
    </CircleWrapper>
  );
};
