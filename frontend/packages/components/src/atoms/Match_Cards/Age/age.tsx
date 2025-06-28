type AgeProps = {
  age: string;
};

export function Age({ age }: AgeProps) {
  return (
    <span className="text-ivory text-2xl font-medium font-ibm_plex_sans">
      {age}
    </span>
  );
}