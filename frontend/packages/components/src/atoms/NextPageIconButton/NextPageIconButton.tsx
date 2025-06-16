interface NextPageIconButtonProps {
  onClick: () => void;
}

export const NextPageIconButton = ({ onClick }: NextPageIconButtonProps) => (
  <button onClick={onClick} className="text-2xl font-bold text-gray-700 hover:text-black">
    &gt;
  </button>
);