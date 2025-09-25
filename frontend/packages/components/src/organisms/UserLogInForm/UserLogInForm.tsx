"use client";

import React from "react";

import { LabeledInputField } from "@molecules/LabeledInputField/LabeledInputField";
import { CircleWrapper } from "@atoms/CircleWrapper/CircleWrapper";
import { KeyIcon } from "lucide-react";
import { NextPageIconButton } from "@atoms";


export interface UserLogInFormProps {
  email: string;
  setEmail: (email: string) => void;
  password: string;
  setPassword: (password: string) => void;
  onSubmit?: () => void;
}

export const UserLoginForm = ({ email, setEmail, password, setPassword, onSubmit }: UserLogInFormProps) => {

  return (
    <CircleWrapper className="bg-amberglow max-md:rounded-none rounded-2xl md:w-xl xl:w-xl text-center aspect-square">
      <div className="flex w-full flex-col items-center py-10 md:absolute md:p-20">
        <h2 className="mb-5 text-2xl font-semibold md:mt-2 md:text-3xl">
          You have returned!
        </h2>
        <LabeledInputField
          label="Email"
          placeholder="Email"
          type="email"
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
        <div className="mt-1">
          <NextPageIconButton onClick={() => onSubmit && onSubmit()} />
          {/* <KeyIcon onClick={() => onSubmit && onSubmit()} /> */}
        </div>
      </div>
    </CircleWrapper>
  );
};
