"use client";

import React from "react";

import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { useState } from "react";
import { Key, KeyIcon } from "lucide-react";

export const LogIn = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  return (
    <div className="flex items-center justify-center min-h-screen">
      <CircleWrapper size="w-[600px] h-[600px]" backgroundColor="bg-amberglow" className="p-8 text-center">
        <div className="w-full px-6 py-8 items-center">
          <div className="w-full flex flex-col items-center">
            <h2 className="text-lg font-semibold mb-6">You have returned!</h2>
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
            <br />

            <KeyIcon onClick={() => console.log({ username })} />
          </div>
        </div>

      </CircleWrapper>
    </div>

  );
};
