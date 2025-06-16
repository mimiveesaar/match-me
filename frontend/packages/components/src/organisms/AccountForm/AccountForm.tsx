"use client";

import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { LabeledMultiLineInput } from "../../molecules/LabeledMultiLineInput/LabeledMultiLineInput";
import { NextPageIconButton } from "../../atoms/NextPageIconButton/NextPageIconButton";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { useState } from "react";

export const AccountForm = () => {
  const [planet, setPlanet] = useState("");
  const [lookingFor, setLookingFor] = useState("");
  const [interests, setInterests] = useState("");
  const [bio, setBio] = useState("");

  return (
        <div className="flex items-center justify-center min-h-screen">
          <CircleWrapper size="w-[600px] h-[600px]" backgroundColor="bg-teal-100 bg-opacity-10" className="p-8 text-center">
      <div className="w-full px-6 py-8 text-center">
        <div className="w-full">
          <h2 className="text-lg font-semibold mb-6">New Account</h2>
          <LabeledInputField
            label="Planet"
            placeholder="Planet of Residency"
            value={planet}
            onChange={(e) => setPlanet(e.target.value)}
          />
          <LabeledInputField
            label="Looking for"
            placeholder="Looking for"
            value={lookingFor}
            onChange={(e) => setLookingFor(e.target.value)}
          />
          <LabeledInputField
            label="Interests"
            placeholder="Interests"
            value={interests}
            onChange={(e) => setInterests(e.target.value)}
          />
          <LabeledMultiLineInput
            placeholder="Bio (optional)"
            value={bio}
            onChange={(e) => setBio(e.target.value)}
          />
          <NextPageIconButton onClick={() => console.log({ planet, lookingFor, interests, bio })} />
        </div>
      </div>

    </CircleWrapper>
        </div>
    
  );
};
