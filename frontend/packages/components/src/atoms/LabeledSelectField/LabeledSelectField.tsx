import React from "react";

interface LabeledSelectFieldProps {
  id: string;
  label: string;
  value: string;
  setValue: (value: string) => void;
  onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  options: { value: string; label: string }[];
}

export function LabeledSelectField({
  id,
  label,
  value,
  setValue,
  onChange,
  options,
}: LabeledSelectFieldProps) {

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setValue(e.target.value);
    onChange(e);
  }

  return (
    <div className="mb-4">
      <label htmlFor={id} className="block text-sm font-medium text-gray-700 mb-1">
        {label}
      </label>
      <select
        id={id}
        value={value}
        onChange={handleChange}
        className="w-full px-4 py-2 rounded-custom-8 drop-shadow-custom bg-ivory focus:outline-none"
      >
        <option value="" disabled>Select {label}</option>
        {options.map(opt => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
    </div>
  );
} 