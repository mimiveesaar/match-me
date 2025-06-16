interface MultiLineInputFieldProps {
  placeholder: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  maxLength?: number;
  rows?: number;
}

export const MultiLineInputField = ({ placeholder, value, onChange, maxLength = 250 , rows = 4}: MultiLineInputFieldProps) => (
  <textarea
    placeholder={placeholder}
    value={value}
    onChange={onChange}
    maxLength={maxLength}
    rows = {rows}
    className="w-full px-4 py-2 border-gray-300 rounded shadow-md shadow-gray-400 focus:outline-none resize-none bg-accent-light"
  />
);
