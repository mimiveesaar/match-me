
import React from "react";

import { useState } from "react";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { CameraIcon } from "../../atoms/CameraIcon/CameraIcon";
import { ProfilePicture } from "../../atoms/ProfilePicture/ProfilePicture";

import { CheckIcon } from "@atoms/CheckIcon/CheckIcon";

export const AccountFormSignUp3 = () => {
  // Add state for the selected image URL
  const [profileImageUrl, setProfileImageUrl] = useState<string | null>(null);

  const handleFileSelected = (file: File) => {
    // Create a temporary URL to preview the image
    const imageUrl = URL.createObjectURL(file);
    setProfileImageUrl(imageUrl);
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <CircleWrapper className="w-[600px] h-[600px] p-8 text-center bg-minty">
        <div className="w-full px-6 py-8 text-center">
          <h2 className="text-lg font-semibold mb-6">Add Profile Picture</h2>

          <div className="flex items-center justify-center mb-4">
            <CameraIcon onFileSelected={handleFileSelected} />
          </div>

          <div className="flex items-center justify-center mb-8">
            {/* Pass the image URL to ProfilePicture to show the preview */}
            <ProfilePicture imageUrl={profileImageUrl} />
          </div>

          <CheckIcon onClick={() => console.log({ profileImageUrl })} />
        </div>
      </CircleWrapper>
    </div>
  );
};
