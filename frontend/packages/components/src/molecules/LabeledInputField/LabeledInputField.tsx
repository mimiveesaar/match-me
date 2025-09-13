"use client";
import React from "react";
import { InputField } from "../../atoms/InputField/InputField";

interface LabeledInputFieldProps {
  id?: string;
  label: string;
  placeholder: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  disabled?: boolean;
}

export const LabeledInputField = ({
  label,
  placeholder,
  value,
  onChange,
  id = `input-${label.replace(/\s+/g, "-").toLowerCase()}`,
  disabled = false,
}: LabeledInputFieldProps) => (
  <div className="mb-4">
    <label
      htmlFor={id}
      className="block text-left text-xs text-gray-500 italic mb-1 ml-1"
    >
      /{label.toLowerCase()}
    </label>
    <InputField
      id={id}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      disabled={disabled}
    />
  </div>
);
