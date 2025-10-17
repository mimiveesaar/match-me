import React from "react";

interface LabeledSelectFieldProps {
  id: string;
  label: string;
  value: string;
  setValue: (value: string) => void;
  onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  options: { value: string; label: string }[];
  error?: string;
}

export function LabeledSelectField({
  id,
  label,
  value,
  setValue,
  onChange,
  options,
  error,
}: LabeledSelectFieldProps) {
  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setValue(e.target.value);
    onChange(e);
  };

  return (
    <div className="mb-4">
      <label htmlFor={id} className="text-ivory mb-1 block text-sm font-medium">
        {label}
      </label>
      <select
        id={id}
        value={value}
        onChange={handleChange}
        className="rounded-custom-8 drop-shadow-custom bg-ivory w-full px-4 py-2 focus:outline-none"
      >
        <option value="" disabled>
          Select {label}
        </option>
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
        {error && (
            <div className="text-sm text-left max-w-full break-words text-red-500 mt-1 ml-">{error}</div>
        )}
    </div>
  );
}
