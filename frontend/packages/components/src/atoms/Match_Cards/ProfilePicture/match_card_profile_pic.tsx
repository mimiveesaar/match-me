type PicProps = {
  src?: string;
  alt?: string;
  className?: string;
};

const DEFAULT_AVATAR = "default-avatar.png";

export function MatchCardProfilePic({ src, alt = "Profile picture", className }: PicProps) {
  const handleError = (event: React.SyntheticEvent<HTMLImageElement, Event>) => {
    event.currentTarget.src = DEFAULT_AVATAR;
  };

  return (
    <div className={`w-265 h-196 rounded-custom_16 overflow-hidden ${className}`}>
      <img
        src={src || DEFAULT_AVATAR}
        alt={alt}
        className="object-cover w-full h-full"
        onError={handleError} // fallback if image fails to load
      />
    </div>
  );
}