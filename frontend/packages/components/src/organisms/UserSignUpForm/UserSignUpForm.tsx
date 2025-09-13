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
      <CircleWrapper className="bg-minty relative rounded-2xl p-12 text-center md:p-60 lg:p-64">
        <div className="flex w-full flex-col items-center py-10 md:absolute md:p-20">
          <h2 className="text-2xl mb-5 font-semibold md:mt-2 md:text-3xl">
            New Account
          </h2>
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

          <NextPageIconButton onClick={() => console.log({ username, mail })} />
        </div>
      </CircleWrapper>
    </div>
  );
};
