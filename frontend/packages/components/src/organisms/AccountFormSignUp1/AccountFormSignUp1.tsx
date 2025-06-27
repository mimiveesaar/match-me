"use client";

import React from "react";

import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { NextPageIconButton } from "../../atoms/NextPageIconButton/NextPageIconButton";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { useState } from "react";

export const AccountFormSignUp1 = () => {
  const [username, setUsername] = useState("");
  const [age, setAge] = useState("");
  const [bodyform, setBodyform] = useState("");
  const [mail, setMail] = useState("");
  const [password, setPassword] = useState(""); 

  return (
    <div className="flex items-center justify-center min-h-screen">
      <CircleWrapper className="w-[600px] h-[600px] p-8 text-center  bg-minty">
        <div className="w-full px-6 py-8 text-center">
          <div className="w-full">
            <h2 className="text-lg font-semibold mb-6">New Account</h2>
            <LabeledInputField
              label="Username"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <LabeledInputField
              label="Age"
              placeholder="Age"
              value={age}
              onChange={(e) => setAge(e.target.value)}
            />
            <LabeledInputField
              label="Bodyform"
              placeholder="Bodyform"
              value={bodyform}
              onChange={(e) => setBodyform(e.target.value)}
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
            
            <NextPageIconButton onClick={() => console.log({ username, age, bodyform, mail })} />
          </div>
        </div>

      </CircleWrapper>
    </div>

  );
};
