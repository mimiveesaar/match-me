type CardProps = {
  color?: "amberglow" | "olive" | "peony" | "minty" | "moss" | "coral";
  children: React.ReactNode;
};

function Card({ color = "minty", children }: CardProps) {
  const bgClassMap = {
    amberglow: "bg-amberglow",
    olive: "bg-olive",
    peony: "bg-peony",
    minty: "bg-minty",
    moss: "bg-moss",
    coral: "bg-coral",
  };

  const bgClass = bgClassMap[color] || bgClassMap.olive;

  return (
    <div
      className={`w-265 h-360 rounded-custom_16 drop-shadow-[-4px_4px_4px_rgba(0,0,0,0.25)] ${bgClass}`}
    >
      {children}
    </div>
  );
}

export function MatchCardBackground({
  color,
  children,
}: CardProps) {
  return <Card color={color}>{children}</Card>;
}