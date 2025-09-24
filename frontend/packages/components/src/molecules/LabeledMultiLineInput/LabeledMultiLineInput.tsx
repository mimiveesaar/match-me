import React from "react";
import { MultiLineInputField } from "@atoms/MultiLineInputField/MultiLineInputField";
import { CharacterCounter } from "@atoms/CharacterCounter/CharacterCounter";

interface LabeledMultiLineInputProps {
  id?: string;
  placeholder?: string;
  value: string;
  setValue: (value: string) => void;
  onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  maxLength?: number;
  disabled?: boolean;
}

export const LabeledMultiLineInput = ({
  id,
  placeholder,
  value,
  setValue,
  onChange,
  disabled,
  maxLength = 250 }: LabeledMultiLineInputProps) => (
  <div className="mb-4">

    <MultiLineInputField
      id={id}
      placeholder={placeholder}
      value={value}
      setValue={setValue}
      onChange={onChange}
      maxLength={maxLength} />

    <CharacterCounter current={value.length} max={maxLength} />
  </div>
);
