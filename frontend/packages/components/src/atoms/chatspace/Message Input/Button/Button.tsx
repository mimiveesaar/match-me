"use client";

import { Send } from 'lucide-react';

interface SendButtonProps {
    onClick?: () => void;
    disabled?: boolean;
}

export const SendButton = ({ onClick, disabled = false }: SendButtonProps) => {
    return (
        <button
            onClick={onClick}
            disabled={disabled}
            className={`flex items-center justify-center w-16 h-12 rounded-custom-8 bg-amberglow drop-shadow-custom-2 hover:scale-110 transition-transform cursor-pointer `}
        >
            <Send
                color="#FFFCF7"
                strokeWidth={2}
                className="w-10 h-10 z-10"
            />
        </button>
    );
};