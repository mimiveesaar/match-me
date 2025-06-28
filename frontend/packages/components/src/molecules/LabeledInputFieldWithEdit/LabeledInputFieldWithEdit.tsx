import React from "react";
import { InputField } from "../../atoms/InputField/InputField";


interface LabeledInputFieldWithEditProps {
  label: string;
  placeholder: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export const LabeledInputFieldWithEdit = ({ label, placeholder, value, onChange }: LabeledInputFieldWithEditProps) => (
  <div className="mb-4">
    <InputField label = {label} placeholder={placeholder} value={value} onChange={onChange} />
  </div>
);
