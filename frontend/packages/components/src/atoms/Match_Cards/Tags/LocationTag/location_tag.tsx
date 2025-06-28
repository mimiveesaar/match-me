type LocationTagProps = {
  location: string;
  className?: string;
};

export function LocationTag({ location, className = "" }: LocationTagProps) {
  return (
    <span className={`h-17 inline-flex items-center gap-2 bg-ivory text-black text-xs font-medium font-noto_serif px-2 rounded-custom_4 ${className}`}>
      📍 {location}
    </span>
  );
}