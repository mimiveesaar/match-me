"use client";

import { ReactNode } from "react";
import { motion, MotionProps } from "framer-motion";

interface CircleWrapperProps extends MotionProps {
    children: ReactNode;
    className?: string;
}

export const CircleWrapper = ({
                                  children,
                                  className = "",
                                  initial,
                                  animate,
                                  transition,
                                  ...rest
                              }: CircleWrapperProps) => {
    return (
        <motion.div
            className={`flex items-center justify-center rounded-full drop-shadow-custom-3 ${className}`}
            initial={initial || { opacity: 0, scale: 0.5 }}
            animate={animate || { opacity: 1, scale: 1 }}
            transition={transition || { duration: 0.8, delay: 0.5, ease: [0, 0.71, 0.2, 1.01] }}
            {...rest}
        >
            {children}
        </motion.div>
    );
};
