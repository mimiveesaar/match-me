type BioTextboxProps = {
  bio: string;
};

export function BioTextbox({ bio }: BioTextboxProps) {
  return (
    <div className="
        w-[233px] h-[61px] bg-ivory bg-opacity-90
        text-black text-xs text-opacity-80 font-ibm_plex_sans 
        rounded-lg shadow-sm overflow-hidden
        drop-shadow-[-2px_2px_2px_rgba(0,0,0,0.25)] ">

      <div className="h-full w-full p-3 overflow-y-auto">
        <p className="whitespace-pre-wrap text-xs leading-snug">
          {bio}
        </p>
      </div>
    </div>
  );
}