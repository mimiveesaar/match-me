interface MenuHeaderProps {
  header: string;
}

export const MenuHeader = ({ header }: MenuHeaderProps) => (
    <span className="text-black text-xl font-sm font-serif pt-4">
      {header}
    </span>
)
