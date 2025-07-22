"use client";

import React from "react";

import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { useState } from "react";
import { KeyIcon } from "@atoms/KeyIcon/KeyIcon";

export const LogIn = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  return (
    <div className="flex min-h-screen items-center justify-center">
      <CircleWrapper className="bg-amberglow relative rounded-2xl p-12 text-center md:p-58 lg:p-64">
        <div className="flex w-full flex-col items-center py-10 md:absolute md:p-20">
          <h2 className="mb-5 text-2xl font-semibold md:mt-2 md:text-3xl">
            You have returned!
          </h2>
          <LabeledInputField
            label="Username"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <LabeledInputField
            label="Password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <div className="mt-1">
            <KeyIcon onClick={() => console.log({ username })} />
          </div>
        </div>
      </CircleWrapper>
    </div>
  );
};
