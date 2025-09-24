import React from "react";

interface MultiLineInputFieldProps {
  id?: string;
  placeholder?: string;
  value: string;
  setValue: (value: string) => void;
  onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  maxLength?: number;
  rows?: number;
}

export function MultiLineInputField({ id, placeholder, value, setValue, onChange, maxLength = 250, rows = 4 }: MultiLineInputFieldProps) {

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    if (onChange) {
      onChange(e);
    }
    setValue(e.target.value);
  }

  return (
    <textarea
      id={id}
      placeholder={placeholder}
      value={value}
      onChange={handleChange}
      maxLength={maxLength}
      rows={rows}
      className="w-full px-4 py-2 border-gray-300 rounded shadow-md shadow-gray-400 focus:outline-none resize-none bg-accent-light"
    />
  );
}