"use client";


interface PhotoBubbleWithUsernameProps {
  src?: string;
  alt?: string;
  username: string;
  isOnline?: boolean;
  onClick: () => void; // required
}

const BACKEND_URL = "http://localhost:8080";
const DEFAULT_AVATAR = `${BACKEND_URL}/images/profiles/default-avatar.png`;

export const PhotoBubbleWithUsername = ({
  src,
  alt,
  username,
  isOnline = false,
  onClick,
}: PhotoBubbleWithUsernameProps) => {

    const handleError = (event: React.SyntheticEvent<HTMLImageElement, Event>) => {
        event.currentTarget.src = DEFAULT_AVATAR;
    };

    const imageUrl = src
        ? (src.startsWith('http') ? src : `${BACKEND_URL}${src}`)
        : DEFAULT_AVATAR;

  return (
    <button
      onClick={onClick}
      className="flex items-center gap-2 mb-2 pl-4 focus:outline-none cursor-pointer"
    >
      <div className="relative">
        <img
          src={imageUrl}
          alt={alt}
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