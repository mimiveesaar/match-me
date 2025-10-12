"use client";

import React, { useState, useRef } from "react";

interface ProfileImageUploadProps {
  currentImage?: string;
  onImageUpdate: (imageFilename: string) => void;
}

export const ProfileImageUpload = ({
  currentImage,
  onImageUpdate,
}: ProfileImageUploadProps) => {
  const [isUploading, setIsUploading] = useState(false);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const API_BASE_URL = "http://localhost:8080/api";

  // Get the full image URL
  const getImageUrl = () => {
    // If we have a preview (during upload), show it
    if (previewUrl) return previewUrl;
    
    // If we have a current image filename
    if (currentImage && currentImage !== "") {
      // Skip placeholder URLs
      if (currentImage.includes("example.com")) {
        return "https://i.imgur.com/0y8Ftya.png";
      }
      
      // If it's already a full URL, return it
      if (currentImage.startsWith("http")) {
        return currentImage;
      }
      
      // Otherwise, it's a filename - fetch from backend with cache-busting
      return `${API_BASE_URL}/profiles/me/image?t=${Date.now()}`;
    }
    
    // Default alien image
    return "https://i.imgur.com/0y8Ftya.png";
  };

  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith("image/")) {
      setError("Please select an image file");
      return;
    }

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

    // Upload file
    await uploadFile(file);
  };

  const uploadFile = async (file: File) => {
    setIsUploading(true);
    setError(null);

    try {
      const formData = new FormData();
      formData.append("file", file);

      const response = await fetch(
        `${API_BASE_URL}/profiles/me/image`,
        {
          method: "POST",
          body: formData,
        }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Failed to upload image");
      }

      const data = await response.json();
      console.log("Upload response:", data);

      // Update parent component with new image filename
      onImageUpdate(data.profilePic);
      
      setError(null);
    } catch (err) {
      console.error("Upload error:", err);
      setError(err instanceof Error ? err.message : "Failed to upload image");
      setPreviewUrl(null);
    } finally {
      setIsUploading(false);
    }
  };

  const handleDeleteImage = async () => {
    if (!currentImage) return;

    // Reset to default
    setPreviewUrl(null);
    onImageUpdate("");
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