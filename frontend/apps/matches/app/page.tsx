import { Menu } from "/workspace/frontend/packages/components/src/organisms/Menu/menu";
import { FlipCard } from "/workspace/frontend/packages/components/src/organisms/MatchCard/FlipCard";
import { MatchCardFront } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardFront";
import { MatchCardBack } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardBack";

export default function Matches() {
  return (
    <div>
      <Menu></Menu>

      <div className="text-pink-700 border border-green-500 p-4 w-40 h-20">Test</div>

      <FlipCard
        front={
          <MatchCardFront location={"Mars"} relationshipType={"Martian lover"}></MatchCardFront>

        } back={
          <MatchCardBack location={"Mars"} relationshipType={"Martian lover"} bodyform={"Gelatinous"} bio={"Hiya!"}></MatchCardBack>
        }>
      </FlipCard>
      
    </div>


  );
}

