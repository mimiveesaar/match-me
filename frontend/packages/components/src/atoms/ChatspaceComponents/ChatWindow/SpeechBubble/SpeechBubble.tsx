"use client";

interface SpeechBubbleProps {
    children?: React.ReactNode;
    color?: "bg-amberglow" | "bg-ivory";
}

export const SpeechBubble = ({ children, color = "bg-ivory" }: SpeechBubbleProps) => {
    const bgColor = color === "bg-amberglow" ? "bg-amberglow/90" : "bg-ivory/90";

    return (
        <div className="flex justify-center items-center px-4">
            <div className="relative w-fit break-words max-w-[257px]">
                {/* Bubble */}
                <div className={`rounded-xl p-4 ${bgColor} text-black/80 chakra-petch font-bold drop-shadow-custom-2 text-xs`}>
                    {children}
                </div>
            </div>
        </div>
    );
};