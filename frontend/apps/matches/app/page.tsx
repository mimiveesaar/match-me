import { Menu } from "/workspace/frontend/packages/components/src/organisms/Menu/menu";
import { FlipCard } from "/workspace/frontend/packages/components/src/organisms/MatchCard/FlipCard";
import { MatchCardFront } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardFront";
import { MatchCardBack } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardBack";
import { AlienMeetLogo } from "/workspace/frontend/packages/components/src/atoms/Alien.meet logo/alien_meet";

export default function Matches() {
  return (
    <div className="flex flex-col items-center pt-6 px-4">
      
      {/* Top: Logo centered */}
      <div>
        <AlienMeetLogo />
      </div>

      {/* Row: Menu + FlipCards */}
      <div className="flex w-full max-w-7xl gap-2">
        
        {/* Left: Menu */}
        <div className="max-h-screen md:max-h-[80vh]">
          <Menu />
        </div>

        {/* Right: FlipCard Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 w-full pt-28 justify-items-center ">
          <FlipCard
            front={<MatchCardFront location="Mars" relationshipType="Martian lover" />}
            back={<MatchCardBack location="Mars" relationshipType="Martian lover" bodyform="Gelatinous" bio="..." />}
          />
          <FlipCard
            front={<MatchCardFront location="Venus" relationshipType="Plasma soulmate" />}
            back={<MatchCardBack location="Venus" relationshipType="Plasma soulmate" bodyform="Vaporous" bio="..." />}
          />
          <FlipCard
            front={<MatchCardFront location="Venus" relationshipType="Plasma soulmate" />}
            back={<MatchCardBack location="Venus" relationshipType="Plasma soulmate" bodyform="Vaporous" bio="..." />}
          />
          <FlipCard
            front={<MatchCardFront location="Venus" relationshipType="Plasma soulmate" />}
            back={<MatchCardBack location="Venus" relationshipType="Plasma soulmate" bodyform="Vaporous" bio="..." />}
          />
        </div>

      </div>
    </div>
  );
}

