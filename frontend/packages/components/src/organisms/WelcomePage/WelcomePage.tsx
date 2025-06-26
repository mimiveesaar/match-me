"use client";

import React from "react";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { SignUpOrLogInButton } from "../../atoms/SignUpOrLogInButton/SignUpOrLogInButton";

export const WelcomePage: React.FC = () => {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <CircleWrapper className="bg-olive p-8 text-center w-[600px] h-[600px]">
        <div className="w-full px-6 py-8 items-center">
          <div className="w-full flex flex-col items-center">
            <h1 className="text-4xl font-serif ">
              <div>Experience the </div>
              <div>otherworldly Joy of a</div> 
              <div>Shared Life</div>
            </h1>
            <br />
            <SignUpOrLogInButton />
          </div>
        </div>
      </CircleWrapper>
    </div>
  );
};