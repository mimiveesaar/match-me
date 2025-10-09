"use client";

import { ReactNode } from "react";
import { motion } from "framer-motion";

interface CircleWrapperProps {
    children: ReactNode;
    className?: string;
}

export const CircleWrapper = ({
                                  children,
                                  className = "",
                              }: CircleWrapperProps) => {
    return (
        <motion.div
            className={`flex items-center justify-center rounded-full drop-shadow-custom-3 ${className}`}
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{
                duration: 0.8,
                delay: 0.5,
                ease: [0, 0.71, 0.2, 1.01],
            }}
        >
            {children}
        </motion.div>
    );
};
