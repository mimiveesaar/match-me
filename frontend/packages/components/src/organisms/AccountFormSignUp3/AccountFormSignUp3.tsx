import React from "react";
import { useState } from "react";
import { CircleWrapper } from "../../atoms/CircleWrapper/CircleWrapper";
import { CameraIcon } from "../../atoms/CameraIcon/CameraIcon";
import { ProfilePicture } from "../../atoms/ProfilePicture/ProfilePicture";
import { CheckIcon } from "../../atoms/CheckIcon/CheckIcon";

interface AccountFormSignUp3Props {
  onSubmit?: (profileImageUrl: string | null) => void | Promise<void>;
  isLoading?: boolean;
}

export const AccountFormSignUp3 = ({
  onSubmit,
  isLoading = false
}: AccountFormSignUp3Props) => {
  const [profileImageUrl, setProfileImageUrl] = useState<string | null>(null);

  const handleFileSelected = (file: File) => {
    const imageUrl = URL.createObjectURL(file);
    setProfileImageUrl(imageUrl);
  };

  const handleSubmit = async () => {
    if (onSubmit) {
      await onSubmit(profileImageUrl);
    } else {
      console.log({ profileImageUrl });
    }
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
            <ProfilePicture imageUrl={profileImageUrl} />
          </div>

          <CheckIcon onClick={handleSubmit} />
        </div>
      </CircleWrapper>
    </div>
  );
};