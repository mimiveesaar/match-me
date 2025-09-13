
"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { AccountFormSignUp2 } from "../../../../../packages/components";
import { apiService, ProfileData } from "../services/api";

export default function SignupPage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const handleSignup = async (formData: ProfileData) => {
    setIsLoading(true);
    
    try {
      const result = await apiService.createProfile(formData);
      console.log('Profile created:', result);
      
      // Redirect to profile page or next step
      router.push('/SignUpPage3');
    } catch (error) {
      console.error('Error creating profile:', error);
      // You might want to show an error message to the user
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <AccountFormSignUp2 
        onSubmit={handleSignup}
        isLoading={isLoading}
      />
    </div>
  );
}