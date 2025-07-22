"use client";

import React from "react";

import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { NextPageIconButton } from "../../atoms/NextPageIconButton/NextPageIconButton";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { useState } from "react";

export const UserSignUpForm = () => {
  const [username, setUsername] = useState("");
  const [mail, setMail] = useState("");
  const [password, setPassword] = useState("");

  return (
    <div className="flex min-h-screen items-center justify-center">
      <CircleWrapper className="bg-minty h-[600px] w-[600px] p-8 text-center">
        <div className="w-full px-6 py-8 text-center">
          <div className="flex w-full flex-col items-center">
            <h2 className="mb-6 text-lg font-semibold">New Account</h2>
            <LabeledInputField
              label="Username"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <LabeledInputField
              label="E-mail"
              placeholder="E-mail"
              value={mail}
              onChange={(e) => setMail(e.target.value)}
            />
            <LabeledInputField
              label="Password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <NextPageIconButton
              onClick={() => console.log({ username, mail })}
            />
          </div>
        </div>
      </CircleWrapper>
    </div>
  );
};
