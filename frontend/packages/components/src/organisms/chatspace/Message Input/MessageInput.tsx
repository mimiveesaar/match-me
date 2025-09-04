"use client";

import { useState } from "react";
import { Background } from "@atoms/chatspace/Message Input/Background/Background";
import { SendButton } from "@atoms/chatspace/Message Input/Button/Button";

interface MessageInputProps {
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  onSend: (message: string) => void;
  disabled?: boolean;
}

export const MessageInput = ({
  value: controlledValue,
  onChange: controlledOnChange,
  onSend,
  disabled = false,
}: MessageInputProps) => {
  const [value, setValue] = useState(controlledValue || "");

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setValue(e.target.value);
    controlledOnChange?.(e);
  };

  const handleSend = () => {
    if (value.trim() === "") return;
    onSend(value);
    setValue("");
  };

  return (
    <div className="w-full flex justify-center items-center py-4">
      <Background
        value={value}
        onChange={handleChange}
        placeholder="Type your message..."
        rightElement={
          <SendButton onClick={handleSend} disabled={disabled || value.trim() === ""} />
        }
      />
    </div>
  );
};