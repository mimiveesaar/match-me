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
  errors?: {
    emailError?: string;
    passwordError?: string;
    confirmPasswordError?: string;
  }
  onSubmit?: () => void;
}

export const UserSignUpForm = ({ email, password, confirmPassword, setEmail, setPassword, setConfirmPassword, errors, onSubmit }: UserSignUpFormProps) => {

  return (
    <CircleWrapper
        className="bg-olive/95 max-md:rounded-custom-16 rounded-2xl md:w-xl xl:w-xl aspect-square text-center mt-4"
        animate={{
            scale: [1, 0.97, 1],
            opacity: [1, 1, 1],
        }}
        transition={{
            duration: 3,
            repeat: Infinity,
            ease: "easeInOut",
        }}


    >
      <div className="flex flex-col items-center py-10 w-full">
        <h2 className="text-2xl mb-5 font-serif md:mt-2 md:text-3xl">
          New Account
        </h2>

        <div className="md:w-96 w-full px-4 font-serif pt-6">
          <LabeledInputField
            label=""
            placeholder="Email"
            value={email}
            error={errors?.emailError}
            onChange={(e) => setEmail(e.target.value)}
          />
          <LabeledInputField
            label=""
            placeholder="Password"
            type="password"
            value={password}
            error={errors?.passwordError}
            onChange={(e) => setPassword(e.target.value)}
          />

          <LabeledInputField
            label=""
            placeholder="Confirm Password"
            value={confirmPassword}
            type="password"
            error={errors?.confirmPasswordError}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </div>

        <NextPageIconButton onClick={() => onSubmit && onSubmit()} />
      </div>
    </CircleWrapper>
  );
};
