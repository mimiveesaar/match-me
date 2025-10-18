"use client";

import React, { useState, useRef, useEffect } from "react";

interface ProfileImageUploadProps {
  currentImage?: string;
  onImageUpdate: (imageFilename: string) => void;
  onImageUpload: (file: File) => Promise<string>;
}

export const ProfileImageUpload = ({
  currentImage,
  onImageUpdate,
  onImageUpload: onImageUpload
}: ProfileImageUploadProps) => {
  const [isUploading, setIsUploading] = useState(false);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [fetchedImageUrl, setFetchedImageUrl] = useState<string | null>(null);


  const fetchProfileImage = async () => {
    if (!currentImage) return;

    try {
      const token = localStorage.getItem("jwtToken");
      const response = await fetch(`${API_BASE_URL}/profiles/me/image`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Failed to fetch profile image");

      const blob = await response.blob();
      setFetchedImageUrl(URL.createObjectURL(blob));
    } catch (err) {
      console.error("❌ Error fetching profile image:", err);
    }
  };

  useEffect(() => {
    if (currentImage && !currentImage.startsWith("http")) {
      fetchProfileImage();
    }
  }, [currentImage]);


  const API_BASE_URL = "http://localhost:8080/api";

  // Get the full image URL
  const getImageUrl = () => {

    if (previewUrl) return previewUrl; // during upload
    if (fetchedImageUrl) return fetchedImageUrl; // blob fetched with token
    if (currentImage && currentImage.startsWith("http")) return currentImage;

    return "https://i.imgur.com/0y8Ftya.png";
  };


  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // Validate file size (5MB)
    if (file.size > 5 * 1024 * 1024) {
      setError("File size must be less than 5MB");
      return;
    }

    // Show preview
    const reader = new FileReader();
    reader.onloadend = () => {
      setPreviewUrl(reader.result as string);
    };
    reader.readAsDataURL(file);

    // Upload file and update state
    try {
      setIsUploading(true);
      const filename = await onImageUpload(file); // capture returned filename
      onImageUpdate(filename); // update profilePic in parent state
    } catch (err) {
      console.error(err);
    } finally {
      setIsUploading(false);
    }
  };


  return (
    <div className="relative w-full mb-4">
      <div className="relative group">
        <img
          src={getImageUrl()}
          alt="Profile"
          className="rounded-custom-16 drop-shadow-custom-2 w-full h-40 object-cover"
        />

        {/* Button appears on hover - no overlay */}
        <div className="absolute inset-0 flex items-center justify-center rounded-custom-16">
          <button
            onClick={() => fileInputRef.current?.click()}
            disabled={isUploading}
            className="opacity-0 group-hover:opacity-100 transition-opacity duration-200 bg-white bg-opacity-90 text-olive font-medium py-1.5 px-3 rounded-lg text-sm shadow-lg"
          >
            {isUploading ? "Uploading..." : "Change"}
          </button>
        </div>
      </div>

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        onChange={handleFileSelect}
        className="hidden"
      />

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded text-xs mt-2">
          {error}
        </div>
      )}
    </div>
  );
};