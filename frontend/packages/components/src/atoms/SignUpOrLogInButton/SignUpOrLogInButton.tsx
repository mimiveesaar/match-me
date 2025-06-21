import React from "react";

export const SignUpOrLogInButton = () => {
  const handleSignUp = () => {
    console.log("Sign Up clicked");
  };

  const handleLogIn = () => {
    console.log("Log In clicked");
  };

  return (
    <div className="text-sm text-black mt-8">
      <button
        onClick={handleSignUp}
        className="appearance-none bg-transparent border-none p-0 m-0 hover:underline focus:outline-none"

      >
        Sign Up
      </button>
      <span className="mx-2">|</span>
      <button
        onClick={handleLogIn}
        className="appearance-none bg-transparent border-none p-0 m-0 hover:underline focus:outline-none"
      >
        Log In
      </button>
    </div>
  );
};
