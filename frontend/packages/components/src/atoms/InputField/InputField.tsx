"use client";
import React from "react";

interface InputFieldProps {
  label?: string;
  id?: string;
  placeholder: string;
  value?: string;
  type?: string;
  disabled?: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export const InputField = ({ id, placeholder, value, onChange, type, disabled }: InputFieldProps) => (
  <input
    id={id}
    type={type ?? "text"}
    placeholder={placeholder}
    value={value}
    onChange={onChange}
    className="w-full px-4 py-2 border-gray-300 rounded-custom-8 drop-shadow-custom focus:outline-none resize-none bg-ivory"
    disabled={disabled}
  />
);


