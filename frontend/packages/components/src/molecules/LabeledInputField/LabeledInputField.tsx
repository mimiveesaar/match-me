"use client";

import React from "react";
import { InputField } from "@atoms";

interface LabeledInputFieldProps {
  id?: string;
  label: string;
  placeholder: string;
  value?: string;
  setValue?: (value: string) => void;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  disabled?: boolean;
  error?: string;
}

export function LabeledInputField({ label, placeholder, disabled, error, id, onChange, setValue, value }: LabeledInputFieldProps) {

  const inputId = id || `input-${label.replace(/\s+/g, "-").toLowerCase()}`;


  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (onChange) {
      onChange(e);
    }
    setValue(e.target.value);
  }

  return (
    <div className="mb-4">
      <label
        htmlFor={inputId}
        className="block text-sm font-medium text-gray-700 mb-1"
      >
        /{label.toLowerCase()}
      </label>
      <InputField
        id={inputId}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        disabled={disabled}
      />
      {error && (
        <div className="text-sm text-red-500 mt-1 ml-">{error}</div>
      )}
    </div>
  );
};
