'use client';

import { useState, useRef } from 'react';
import { Background } from '@atoms/chatspace/Message Input/Background/Background';
import { SendButton } from '@atoms/chatspace/Message Input/Button/Button';

interface MessageInputProps {
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  onSend: (message: string) => void;
  onTyping?: (isTyping: boolean) => void; // callback for typing
  disabled?: boolean;
}

export const MessageInput = ({
  value: controlledValue,
  onChange: controlledOnChange,
  onSend,
  onTyping,
  disabled = false,
}: MessageInputProps) => {
  const [value, setValue] = useState(controlledValue || '');
  const typingTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setValue(e.target.value);
    controlledOnChange?.(e);

    // Notify typing start
    onTyping?.(true);

    // Debounce typing stop
    if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);
    typingTimeoutRef.current = setTimeout(() => {
      onTyping?.(false);
    }, 1000);
  };

  const handleSend = () => {
    if (value.trim() === '') return;
    onSend(value);
    setValue('');

    // Stop typing immediately when sending
    onTyping?.(false);
  };

  return (
    <div className="w-full flex justify-center items-center py-4">
      <Background
        value={value}
        onChange={handleChange}
        placeholder="Type your message..."
        rightElement={
          <SendButton
            onClick={handleSend}
            disabled={disabled || value.trim() === ''}
          />
        }
      />
    </div>
  );
};