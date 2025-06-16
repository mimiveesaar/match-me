import { InputField } from "../../atoms/InputField/InputField";


interface LabeledInputFieldProps {
  label: string;
  placeholder: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export const LabeledInputField = ({ label, placeholder, value, onChange }: LabeledInputFieldProps) => (
  <div className="mb-4">
    <InputField placeholder={placeholder} value={value} onChange={onChange} />
  </div>
);
