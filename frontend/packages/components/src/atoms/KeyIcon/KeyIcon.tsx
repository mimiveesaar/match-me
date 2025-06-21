import React from "react";
import { useRef } from "react";
import { Key } from "lucide-react";

export const KeyIcon = ({ onFileSelected }: { onFileSelected?: (file: File) => void }) => {
  const inputRef = useRef<HTMLInputElement>(null);

  const handleButtonClick = () => {
    inputRef.current?.click();
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files = event.target.files;
    if (files && files.length > 0) {
      // Pass the selected file to the parent or do something with it
      onFileSelected?.(files[0]);
    }
  };

  return (
    <>
      <button
        type="button"
        onClick={handleButtonClick}
        className="p-2 rounded hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
        aria-label="Log in"
      >
        <Key  />
      </button>
      <input
        type="file"
        accept="image/*"
        ref={inputRef}
        onChange={handleFileChange}
        style={{ display: "none" }}
      />
    </>
  );
};
