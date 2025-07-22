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
      <CircleWrapper className="bg-amberglow rounded-2xl p-8 text-center">
        <div className="flex w-full flex-col items-center p-10">
            <h2 className="mb-5 text-2xl font-semibold">You have returned!</h2>
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
            <KeyIcon onClick={() => console.log({ username })} />
        </div>
      </CircleWrapper>
    </div>
  );
};
