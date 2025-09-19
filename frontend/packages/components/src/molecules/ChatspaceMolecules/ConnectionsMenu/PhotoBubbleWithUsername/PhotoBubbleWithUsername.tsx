"use client";


interface PhotoBubbleWithUsernameProps {
  src?: string;
  alt?: string;
  username: string;
  isOnline?: boolean;
  onClick: () => void; // required
}

export const PhotoBubbleWithUsername = ({
  src,
  alt,
  username,
  isOnline = false,
  onClick,
}: PhotoBubbleWithUsernameProps) => {
  return (
    <button
      onClick={onClick}
      className="flex items-center gap-2 mb-2 pl-4 focus:outline-none cursor-pointer"
    >
      <div className="relative">
        <img
          src={src || '/default-avatar.png'}
          alt={alt || username}
          className="w-10 h-10 rounded-full object-cover"
        />
        {isOnline && (
          <span className="absolute bottom-0 right-0 w-2 h-2 bg-limeburst rounded-full" />
        )}
      </div>
      <div className="chakra-petch text-ivory">{username}</div>
    </button>
  );
};