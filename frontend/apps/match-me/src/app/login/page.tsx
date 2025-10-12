"use client"

import axios from "axios";
import { UserLoginForm } from "components"
import { useState } from "react";


export default function LoginPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [errors, setErrors] = useState({
        emailError: "",
        passwordError: ""
    });

    function handleLogin() {
        let hasErrors = false;
        console.log("Logging in user with email:", email);

        // Reset errors
        setErrors({
            emailError: "",
            passwordError: ""
        });

        if (!email) {
            setErrors((prev) => ({ ...prev, emailError: "Email is required!" }));
            hasErrors = true;
        }
        if (!password) {
            setErrors((prev) => ({ ...prev, passwordError: "Password is required!" }));
            hasErrors = true;
        }

        if (hasErrors) {
            return;
        }

        // Proceed with login logic (e.g., API call)
        console.log("Proceeding with login for:", email);

        var url = `${process.env.NEXT_PUBLIC_API_HOST}/api/v1/user-management/login`;


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
            console.log("Login successful:", response.data);
        }).catch(error => {
            console.error("Login error:", error.response.data);
            // Handle login error (e.g., show error message)
        }
        );
    }

    return (
        <UserLoginForm
            email={email}
            setEmail={setEmail}
            password={password}
            setPassword={setPassword}
            errors={errors}
            onSubmit={() => handleLogin()}
        >
        </UserLoginForm>
    )
}