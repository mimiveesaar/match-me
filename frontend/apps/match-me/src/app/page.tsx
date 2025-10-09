"use client";


import { CircleWrapper, SignUpButton } from "components/atoms";
import { useEffect } from "react";
import TypeWriter from "typewriter-effect";

export default function Home() {

    useEffect(() => {
        document.title = 'Welcome | alien.meet'
    }, [])

    return (
        <div className="flex min-h-screen items-center justify-center bg-ivory">
            <CircleWrapper className="bg-olive/95 max-md:rounded-custom-16 rounded-2xl md:w-xl xl:w-xl aspect-square text-center mt-4 flex flex-col">

                {/* Centered text */}
                <div className="flex flex-1 items-center justify-center">
                    <div className="text-2xl font-serif md:mt-10 md:text-3xl leading-snug">
                        <TypeWriter options={{
                            strings: [  'Are you ready<br>to meet your next<br>Intergalactic Lover?',
                                        'Experience the<br>Otherworldly Joy of a<br>Shared Life',
                                        'Sign up now<br>to find someone<br>who abducts your heart'],
                            autoStart: true,
                            loop: true,
                            cursor: '_',
                        }}
                        >

                        </TypeWriter>
                    </div>
                </div>

                {/* Button pinned at bottom */}
                <div className="mb-6 flex justify-center">
                    <SignUpButton />
                </div>
            </CircleWrapper>
        </div>
    )

}