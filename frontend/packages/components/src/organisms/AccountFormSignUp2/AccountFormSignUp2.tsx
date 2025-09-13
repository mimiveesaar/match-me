
"use client";

import React from "react";
import { LabeledInputField } from "../../molecules/LabeledInputField/LabeledInputField";
import { LabeledMultiLineInput } from "../../molecules/LabeledMultiLineInput/LabeledMultiLineInput";
import { NextPageIconButton } from "../../atoms/NextPageIconButton/NextPageIconButton";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { useState } from "react";

interface AccountFormSignUp2Props {
  onSubmit?: (data: {
    planet: string;
    lookingFor: string;
    interests: string;
    bio: string;
  }) => void | Promise<void>;
  isLoading?: boolean;
}

export const AccountFormSignUp2 = ({
  onSubmit,
  isLoading = false
}: AccountFormSignUp2Props) => {
  const [planet, setPlanet] = useState("");
  const [lookingFor, setLookingFor] = useState("");
  const [interests, setInterests] = useState("");
  const [bio, setBio] = useState("");

  const handleSubmit = async () => {
    const formData = { planet, lookingFor, interests, bio };

    if (onSubmit) {
      await onSubmit(formData);
    } else {
      console.log(formData);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <CircleWrapper
        className="w-[600px] h-[600px] p-8 text-center"
        style={{ backgroundColor: '#D2F0EA' }}
      >
        <div className="w-full px-6 py-8 text-center">
          <div className="w-full">
            <h2 className="text-lg font-semibold mb-6">New Account</h2>
            <LabeledInputField
              label="Planet"
              placeholder="Planet of Residency"
              value={planet}
              onChange={(e) => setPlanet(e.target.value)}
              disabled={isLoading}
            />
            <LabeledInputField
              label="Looking for"
              placeholder="Looking for"
              value={lookingFor}
              onChange={(e) => setLookingFor(e.target.value)}
              disabled={isLoading}
            />
            <LabeledInputField
              label="Interests"
              placeholder="Interests"
              value={interests}
              onChange={(e) => setInterests(e.target.value)}
              disabled={isLoading}
            />
            <LabeledMultiLineInput
              id="bio"
              placeholder="Bio (optional)"
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              disabled={isLoading}
            />
            <NextPageIconButton
              onClick={handleSubmit}
              disabled={isLoading || !planet || !lookingFor}
            />
          </div>
        </div>
      </CircleWrapper>
    </div>
  );
};