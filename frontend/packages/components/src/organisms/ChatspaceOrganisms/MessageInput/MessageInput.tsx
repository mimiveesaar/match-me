'use client';

import { useState, useRef } from 'react';
import { MessageInputBackground, MessageInputSendButton} from 'src/atoms';


import React from 'react';

interface MessageInputProps {
    value?: string;
    onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
    onSend: (message: string) => void;
    onTyping?: (isTyping: boolean) => void;
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

        onTyping?.(true);

        if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);
        typingTimeoutRef.current = setTimeout(() => {
            onTyping?.(false);
        }, 1000);
    };

    const handleSend = () => {
        if (value.trim() === '') return;
        onSend(value.trim());
        setValue('');
        onTyping?.(false);
    };

    // New: Handle Enter key press for sending message
    const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (
            e.key === 'Enter' &&
            !e.shiftKey &&  // allow Shift+Enter for newline
            !e.ctrlKey &&
            !disabled
        ) {
            e.preventDefault(); // prevent new line
            handleSend();
        }
    };

    return (
        <div className="w-full flex justify-center items-center py-2 lg:py-4">
            <MessageInputBackground
                value={value}
                onChange={handleChange}
                onKeyDown={handleKeyDown}
                placeholder="Type your message..."
                rightElement={
                    <MessageInputSendButton
                        onClick={handleSend}
                        disabled={disabled || value.trim() === ''}
                    />
                }
            />
        </div>
    );
};
