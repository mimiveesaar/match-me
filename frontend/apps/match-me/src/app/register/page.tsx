"use client"

import axios from "axios";
import { UserSignUpForm } from "components/organisms"
import { useState } from "react";

export default function RegisterPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [errors, setErrors] = useState({
        emailError: "",
        passwordError: "",
        confirmPasswordError: ""
    });

    function handleRegister() {
        let hasErrors = false;
        console.log("Registering user with email:", email);

        // Reset errors
        setErrors({
            emailError: "",
            passwordError: "",
            confirmPasswordError: ""
        });

        if (!email) {
            setErrors((prev) => ({ ...prev, emailError: "Email is required!" }));
            hasErrors = true;
        }
        if (!password) {
            setErrors((prev) => ({ ...prev, passwordError: "Password is required!" }));
            hasErrors = true;
        }

        if (password !== confirmPassword) {
            setErrors((prev) => ({ ...prev, passwordError: "Passwords do not match!", confirmPasswordError: "Passwords do not match!" }));
            hasErrors = true;

        }

        if (hasErrors) {
            return;
        }

        axios(
            {
                method: 'post',
                url: '/api/v1/user-management/register',
                data: {
                    email,
                    password
                }
            }
        )

        // Proceed with registration logic (e.g., API call)
    }

    return (
        <UserSignUpForm
            email={email}
            setEmail={setEmail}
            password={password}
            setPassword={setPassword}
            confirmPassword={confirmPassword}
            setConfirmPassword={setConfirmPassword}
            errors={errors}
            onSubmit={handleRegister} />
    );
}