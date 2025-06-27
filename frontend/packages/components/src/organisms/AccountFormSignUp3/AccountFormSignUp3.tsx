
import React from "react";

import { useState } from "react";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { CameraIcon } from "../../atoms/CameraIcon/CameraIcon";
import { ProfilePicture } from "../../atoms/ProfilePicture/ProfilePicture";
import { NextPageIconButton } from "../../atoms/NextPageIconButton/NextPageIconButton";

export const AccountFormSignUp3 = () => {
  const [planet, setPlanet] = useState("");
  const [lookingFor, setLookingFor] = useState("");
  const [interests, setInterests] = useState("");
  const [bio, setBio] = useState("");

  // Add state for the selected image URL
  const [profileImageUrl, setProfileImageUrl] = useState<string | null>(null);

  const handleFileSelected = (file: File) => {
    // Create a temporary URL to preview the image
    const imageUrl = URL.createObjectURL(file);
    setProfileImageUrl(imageUrl);

    // Optional: If you want to revoke URL later to avoid memory leaks
    // you can do so when the component unmounts or the image changes
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <CircleWrapper className="w-[600px] h-[600px] p-8 text-center">
        <div className="w-full px-6 py-8 text-center">
          <h2 className="text-lg font-semibold mb-6">Add Profile Picture</h2>

          <div className="flex items-center justify-center mb-4">
            {/* Pass the handler here */}
            <CameraIcon onFileSelected={handleFileSelected} />
          </div>

          <div className="flex items-center justify-center mb-8">
            {/* Pass the image URL to ProfilePicture to show the preview */}
            <ProfilePicture imageUrl={profileImageUrl} />
          </div>

          {/* Other input fields, e.g. planet, lookingFor... */}

          <NextPageIconButton onClick={() => console.log({ planet, lookingFor, interests, bio, profileImageUrl })} />
        </div>
      </CircleWrapper>
    </div>
  );
};
