import { MultiLineInputField } from "../../atoms/MultiLineInputField/MultiLineInputField";
import { CharacterCounter } from "../../atoms/CharacterCounter/CharacterCounter";

interface LabeledMultiLineInputProps {
  placeholder: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  maxLength?: number;
}

export const LabeledMultiLineInput = ({ placeholder, value, onChange, maxLength = 250 }: LabeledMultiLineInputProps) => (
  <div className="mb-4">
    <MultiLineInputField placeholder={placeholder} value={value} onChange={onChange} maxLength={maxLength} />
    <CharacterCounter current={value.length} max={maxLength} />
  </div>
);
