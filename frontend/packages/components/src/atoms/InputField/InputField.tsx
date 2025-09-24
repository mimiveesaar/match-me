"use client";
import React from "react";

interface InputFieldProps {
  label?: string;
  id?: string;
  placeholder: string;
  value?: string;
  disabled?: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export const InputField = ({ id, placeholder, value, onChange, disabled }: InputFieldProps) => (
  <input
    id={id}
    type="text"
    placeholder={placeholder}
    value={value}
    onChange={onChange}
    className="w-full px-4 py-2 border-gray-300 rounded shadow-md shadow-gray-400 focus:outline-none resize-none bg-accent-light"
    disabled={disabled}
  />
);


