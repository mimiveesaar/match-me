"use client";
import { ReactNode } from "react";

interface InputFieldProps {
  placeholder: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export const InputField = ({ placeholder, value, onChange }: InputFieldProps) => (
  <input
    type="text"
    placeholder={placeholder}
    value={value}
    onChange={onChange}
    className="w-full px-4 py-2 border-gray-300 rounded shadow-md shadow-gray-400 focus:outline-none resize-none bg-accent-light"

  />
);





