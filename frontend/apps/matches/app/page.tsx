import { Menu } from "/workspace/frontend/packages/components/src/organisms/Menu/menu";
import { FlipCard } from "/workspace/frontend/packages/components/src/organisms/MatchCard/FlipCard";
import { MatchCardFront } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardFront";
import { MatchCardBack } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardBack";

export default function Matches() {
  return (
    <div className="flex">
      <Menu></Menu>

      <div className="grid grid-cols-3 grid-rows-2 gap-4 pt-28">
        <FlipCard
          front={
            <MatchCardFront location={"Mars"} relationshipType={"Martian lover"}></MatchCardFront>

          } back={
            <MatchCardBack location={"Mars"} relationshipType={"Martian lover"} bodyform={"Gelatinous"} bio={"Hiya! mignoig, fkjj  jrnwronfiownf enfjJFBrofbjfnjrwwnfj . mf grigbWGBN  FmrlkrngRGJ NSSMW. FHEUWFnfjefowiFNJF. KFNKNFKW , KSNNnfnbgruigbv fjvskjvosaldnnfsj, fsknflkwhhffslkd.g."}></MatchCardBack>
          }>
        </FlipCard>

        <FlipCard
          front={
            <MatchCardFront location={"Mars"} relationshipType={"Martian lover"}></MatchCardFront>

          } back={
            <MatchCardBack location={"Mars"} relationshipType={"Martian lover"} bodyform={"Gelatinous"} bio={"Hiya! mignoig, fkjj  jrnwronfiownf enfjJFBrofbjfnjrwwnfj . mf grigbWGBN  FmrlkrngRGJ NSSMW. FHEUWFnfjefowiFNJF. KFNKNFKW , KSNNnfnbgruigbv fjvskjvosaldnnfsj, fsknflkwhhffslkd.g."}></MatchCardBack>
          }>
        </FlipCard>

        <FlipCard
          front={
            <MatchCardFront location={"Mars"} relationshipType={"Martian lover"}></MatchCardFront>

          } back={
            <MatchCardBack location={"Mars"} relationshipType={"Martian lover"} bodyform={"Gelatinous"} bio={"Hiya! mignoig, fkjj  jrnwronfiownf enfjJFBrofbjfnjrwwnfj . mf grigbWGBN  FmrlkrngRGJ NSSMW. FHEUWFnfjefowiFNJF. KFNKNFKW , KSNNnfnbgruigbv fjvskjvosaldnnfsj, fsknflkwhhffslkd.g."}></MatchCardBack>
          }>
        </FlipCard>
      </div>


    </div>


  );
}

