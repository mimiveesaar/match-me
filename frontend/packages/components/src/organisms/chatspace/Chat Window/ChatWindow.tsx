"use client";

import { Background } from "@atoms/chatspace/chat-window/Background/Background";
import { SpeechBubbleWithHeader } from "@molecules/chatspace/chat-window/speech-bubble-with-header/SpeechBubbleWithHeader";

export const ChatWindow = () => {
    return (
        <Background>
            <div className="flex flex-col space-y-4 p-4">
                <div className="flex justify-start pr-90"> {/* Left-aligned */}
                    <SpeechBubbleWithHeader
                        sender="Zorbplat"
                        time="1:11 PM"
                        date="6/9/25"
                        bubbleColor="bg-ivory"
                    >

                        Greetings! Your neural aura caught my attention. Are those your real tentacles in your profile pic?

                    </SpeechBubbleWithHeader>
                </div>

                <div className="flex justify-end"> {/* Right-aligned */}
                    <SpeechBubbleWithHeader
                        sender="Me"
                        time="1:12 PM"
                        date="6/9/25"
                        bubbleColor="bg-amberglow"
                    >

                        LOL yes! All 7 are mine — no photoplasma filters.  You had me at “loves asteroid picnics.”

                    </SpeechBubbleWithHeader>
                </div>

                <div className="flex justify-start"> {/* Left-aligned */}
                    <SpeechBubbleWithHeader
                        sender="Zorbplat"
                        time="1:13 PM"
                        date="6/9/25"
                        bubbleColor="bg-ivory"
                    >

                        Then perhaps a meteor shower over Kepler-452b? I’ll bring the antimatter fondue.

                    </SpeechBubbleWithHeader>
                </div>

                <div className="flex justify-end"> {/* Right-aligned */}
                    <SpeechBubbleWithHeader
                        sender="Me"
                        time="1:14 PM"
                        date="6/9/25"
                        bubbleColor="bg-amberglow"
                    >
                        It’s a date! Just need to check my schedule with my space-time continuum first.

                    </SpeechBubbleWithHeader>
                </div>

                <div className="flex justify-end"> {/* Right-aligned */}
                    <SpeechBubbleWithHeader
                        sender=""
                        time=""
                        date=""
                        bubbleColor="bg-amberglow"
                    >
                        ...

                    </SpeechBubbleWithHeader>
                </div>
            </div>
        </Background>
    );
}