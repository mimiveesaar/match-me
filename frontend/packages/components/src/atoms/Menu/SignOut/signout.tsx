"use client";

import React from "react";

export const SignOutButton = () => (
    <button
        type='button'
        className="flex items-center justify-center w-249 h-9 hover:scale-125 transition-transform cursor-pointer text-xs font-serif"
        onClick={() => alert(`You have been signed out!`)}
    >
        Sign Out
    </button>
)