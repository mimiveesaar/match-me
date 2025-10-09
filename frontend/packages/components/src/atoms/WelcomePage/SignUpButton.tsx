"use client";

import Link from "next/link";
import React from "react";

export const SignUpButton = () => (
    <Link href="/register">
        <button
            type='button'
            className="flex items-center justify-center w-249 h-9 hover:scale-110 transition-transform cursor-pointer text-sm font-serif"
        >
            Sign Up
        </button>
    </Link>

)