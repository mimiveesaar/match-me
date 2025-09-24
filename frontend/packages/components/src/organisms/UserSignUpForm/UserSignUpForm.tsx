"use client";

import { LabeledInputField } from "@molecules/LabeledInputField/LabeledInputField";
import { NextPageIconButton } from "@atoms/NextPageIconButton/NextPageIconButton";
import { CircleWrapper } from "@atoms/CircleWrapper/CircleWrapper";


export interface UserSignUpFormProps {
  email: string;
  setEmail: (email: string) => void;
  password: string;
  setPassword: (password: string) => void;
  onSubmit?: () => void;
}

export const UserSignUpForm = ({ email, password, setEmail, setPassword, onSubmit }: UserSignUpFormProps) => {

  return (
    <CircleWrapper className="bg-minty max-lg:rounded-none rounded-2xl w-full h-full lg:w-xl xl:w-xl aspect-square text-center">
      <div className="flex flex-col items-center py-10 md:p-20 ">
        <h2 className="text-2xl mb-5 font-semibold md:mt-2 md:text-3xl">
          New Account
        </h2>
        <LabeledInputField
          label="E-mail"
          placeholder="E-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <LabeledInputField
          label="Password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <NextPageIconButton onClick={() => onSubmit()} />
      </div>
    </CircleWrapper>
  );
};
