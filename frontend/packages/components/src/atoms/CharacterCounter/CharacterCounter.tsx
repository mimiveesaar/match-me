interface CharacterCounterProps {
  current: number;
  max: number;
}

export const CharacterCounter = ({ current, max }: CharacterCounterProps) => (
  <div className="text-xs text-gray-500 text-right">{`${current} / ${max}`}</div>
);
