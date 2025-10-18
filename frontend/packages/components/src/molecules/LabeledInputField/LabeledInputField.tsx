"use client";

import React from "react";
import { InputField } from "@atoms";

interface LabeledInputFieldProps {
  id?: string;
  label: string;
  placeholder: string;
  value?: string;
  type?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  disabled?: boolean;
  error?: string;
}

export function LabeledInputField({ label, placeholder, disabled, error, type, id, onChange, value }: LabeledInputFieldProps) {

  const inputId = id || `input-${label.replace(/\s+/g, "-").toLowerCase()}`;

  return (
    <div className="mb-4">
      <label
        htmlFor={inputId}
        className="block text-sm text-left font-medium text-ivory mb-1"
      >
        {label.toLowerCase()}
      </label>
      <InputField
        id={inputId}
        placeholder={placeholder}
        value={value}
        type={type}
        onChange={onChange}
        disabled={disabled}
      />

          <div className="text-sm text-left max-w-full break-words text-red-500 mt-1 ml-">{error}</div>

    </div>
  );
};
