"use client";

import { useState, useEffect } from "react";
import { Menu } from "/workspace/frontend/packages/components/src/organisms/Menu/menu";
import { FlipCard } from "/workspace/frontend/packages/components/src/organisms/MatchCard/FlipCard";
import { MatchCardFront } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardFront";
import { MatchCardBack } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardBack";
import { AlienMeetLogo } from "/workspace/frontend/packages/components/src/atoms/Alien.meet logo/alien_meet";
import { useUserSearch } from "/workspace/frontend/apps/matches/app/hooks/useUserSearch";
import { MatchUser, Filters } from "../types";


export default function Matches() {

  const [filters, setFilters] = useState<Filters>({
    minAge: 18,       
    maxAge: 150,
    maxDistanceLy: 150,
    bodyform: "",
    interests: [],
    lookingFor: "",
    homeplanet: "",
  } as Filters);

  // Custom hook to fetch filtered users
  const userId = "c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2"; // replace with real logged-in user later
  const { users: fetchedUsers, isLoading } = useUserSearch(userId, filters);

  // Local UI state (e.g., for hiding cards)
  const [users, setUsers] = useState<MatchUser[]>([]);

  // When filters change → update local state
  useEffect(() => {
    setUsers(fetchedUsers);
  }, [fetchedUsers]);

  // Hide user handler
  const handleHideUser = (id: string) => {
    setUsers(prev => prev.filter(user => user.id !== id));
  };

  return (
    <div className="flex flex-col items-center pt-4 px-4">
      <div>
        <AlienMeetLogo />
      </div>

      <div className="flex w-full max-w-7xl gap-2">
        {/* Left: Filter menu */}
        <div className="max-h-screen md:max-h-[80vh]">
          <Menu filters={filters} setFilters={setFilters} />
        </div>

        {/* Right: Match cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 w-full pt-28 justify-items-center">
          {isLoading ? (
            <div>Loading...</div>
          ) : (
            users.map((user) => (
              <FlipCard
                key={user.id}
                front={
                  <MatchCardFront
                    username={user.username}
                    age={user.age}
                    location={user.homeplanet}
                    lookingFor={user.lookingFor}
                    bio={user.bio ?? "..."}
                  />
                }
                back={
                  <MatchCardBack
                    username={user.username}
                    age={user.age}
                    location={user.homeplanet}
                    lookingFor={user.lookingFor}
                    bodyform={user.bodyform}
                    bio={user.bio ?? "..."}
                    interests={user.interests ?? []}
                    onHide={() => handleHideUser(user.id)}
                  />
                }
              />
            ))
          )}
        </div>
      </div>
    </div>
  );
}