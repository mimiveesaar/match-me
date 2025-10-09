"use client"

import axios from "axios";
import { UserSignUpForm } from "components/organisms"
import {useEffect, useState } from "react";
import { useRouter } from 'next/navigation';
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";


export default function RegisterPage() {

    useEffect(() => {
        document.title = 'Sign Up | alien.meet'
    }, [])

    const router = useRouter();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [errors, setErrors] = useState({
        emailError: "",
        passwordError: "",
        confirmPasswordError: ""
    });

    async function handleRegister(router: AppRouterInstance) {
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

        var url = `${process.env.NEXT_PUBLIC_API_HOST}/api/v1/user-management/register`;
        axios(
            {
                method: 'post',
                url: url,
                data: {
                    email,
                    password
                }
            }
        ).then(response => {
            console.log("Registration successful:", response.data);
            router.push('/login');
        }).catch(error => {
            console.error("Registration error:", error.response.data);

            var responseData = error.response.data;
            if (responseData.type === "INVALID_REQUEST") {
                responseData.data.errors.forEach((err: { field: string; message: string; rejected_value: string }) => {

                    if (err.field === "email.value") {
                        setErrors((prev) => ({ ...prev, emailError: err.message }));
                    }

                    if (err.field === "password.value") {
                        setErrors((prev) => ({ ...prev, passwordError: err.message, confirmPasswordError: err.message }));
                    }
                });

                return;
            }

            if (responseData.type === "EMAIL_EXISTS") {
                setErrors((prev) => ({ ...prev, emailError: "Email already exists!" }));
                return;
            }

            var errorData = error.response.data.data;

        });
    }

    return (
        <div className="flex h-full items-center justify-center bg-ivory">
            <UserSignUpForm
                email={email}
                setEmail={setEmail}
                password={password}
                setPassword={setPassword}
                confirmPassword={confirmPassword}
                setConfirmPassword={setConfirmPassword}
                errors={errors}
                onSubmit={() => handleRegister(router)}
            />
        </div>
    );
}