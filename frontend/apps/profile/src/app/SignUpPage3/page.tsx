"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { AccountFormSignUp3 } from "../../../../../packages/components";

export default function SignupPage3() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const handleProfilePicSubmit = async (profileImageUrl: string | null) => {
    if (!profileImageUrl) {
      console.log("No image selected, proceeding anyway");
      router.push('/my-profile');
      return;
    }

    setIsLoading(true);
    
    try {
      // For now, just log and proceed - you can add image upload logic later
      console.log('Profile picture URL:', profileImageUrl);
      
      // Navigate to the profile page
      router.push('/my-profile');
    } catch (error) {
      console.error('Error uploading profile picture:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <AccountFormSignUp3 
        onSubmit={handleProfilePicSubmit}
        isLoading={isLoading}
      />
    </div>
  );
}