"use client";

import { useState, useEffect } from "react";
import { Menu } from "/workspace/frontend/packages/components/src/organisms/Menu/menu";
import { FlipCard } from "/workspace/frontend/packages/components/src/organisms/MatchCard/FlipCard";
import { MatchCardFront } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardFront";
import { MatchCardBack } from "/workspace/frontend/packages/components/src/organisms/MatchCard/MatchCardBack";
import { AlienMeetLogo } from "/workspace/frontend/packages/components/src/atoms/Alien.meet logo/alien_meet";
import { useUserSearch } from "/workspace/frontend/apps/matches/app/hooks/useUserSearch";

interface MatchUser {
  id: string;
  location: string;
  relationshipType: string;
  bodyform: string;
  bio?: string;
}

export default function Matches() {

  const [filters, setFilters] = useState({
    ageRange: [18, 1000] as [number, number],
    distanceRange: [0, 9300] as [number, number],
    bodyform: [],
    interests: [],
    lookingFor: [],
  });

  // Custom hook to fetch filtered users
  const { users: fetchedUsers, isLoading } = useUserSearch(filters);

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
                    location={user.location}
                    relationshipType={user.relationshipType}
                  />
                }
                back={
                  <MatchCardBack
                    location={user.location}
                    relationshipType={user.relationshipType}
                    bodyform={user.bodyform}
                    bio={user.bio ?? "..."}
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