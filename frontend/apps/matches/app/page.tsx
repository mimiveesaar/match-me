"use client";

import { useState, useEffect } from "react";
import { Menu } from "@organisms/Menu/Menu";
import { FlipCard } from "@organisms/MatchCard/FlipCard";
import { MatchCardFront, MatchCardFrontProps } from "@organisms/MatchCard/MatchCardFront";
import { MatchCardBack } from "@organisms/MatchCard/MatchCardBack";
import { AlienMeetLogo } from "@atoms/AlienMeetLogo/AlienMeetLogo";
import { useUserSearch } from "./hooks/useUserSearch";
import { MatchUser, Filters } from "../types";


const lookingForColors: Record<string, MatchCardFrontProps["cardColor"]> = {
  Friendship: "amberglow",
  Romance: "coral",
  "Strategic Alliance": "peony",
  "Co-parenting Hatchlings": "minty",
  "Host Symbiosis": "moss",
  Chtulhu: "olive",
};

export default function Matches() {
  const userId = "d87e7304-7bfb-4bfb-9318-52c58f3c1034"; // replace with real logged-in user later

  const [filters, setFilters] = useState<Filters>({
    minAge: 18,
    maxAge: 150,
    maxDistanceLy: 340,
    bodyform: "",
    interests: [],
    lookingFor: "",
    homeplanet: "",
  } as Filters);

  // Fetch matches + currentUser in one hook
  const { users: fetchedUsers, currentUser, isLoading } = useUserSearch(
    userId,
    filters
  );

  // Initialize homeplanet once currentUser is available
  useEffect(() => {
    if (currentUser?.homeplanet && !filters.homeplanet) {
      setFilters((f) => ({ ...f, homeplanet: currentUser.homeplanet }));
    }
  }, [currentUser, filters.homeplanet]);

  const [visibleUsers, setVisibleUsers] = useState<MatchUser[]>([]);
  const [remainingUsers, setRemainingUsers] = useState<MatchUser[]>([]);
  const [showMobileMenu, setShowMobileMenu] = useState(false);

  // When filters change → update local state
  useEffect(() => {
    if (fetchedUsers.length > 0) {
      const initial = fetchedUsers.slice(0, 6);
      const rest = fetchedUsers.slice(6);
      setVisibleUsers(initial);
      setRemainingUsers(rest);
    } else {
      setVisibleUsers([]);
      setRemainingUsers([]);
    }
  }, [fetchedUsers]);

  const removeUserAndRefill = (userId: string) => {
    setVisibleUsers((prev) => {
      const updated = prev.filter((u) => u.id !== userId);

      if (remainingUsers.length > 0) {
        const [next, ...rest] = remainingUsers;
        setRemainingUsers(rest);
        return [...updated, next];
      }

      return updated;
    });
  };

  const handleReject = async (rejectedId: string) => {
    try {
      const res = await fetch(
        `http://localhost:8080/api/rejections/${userId}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ rejected_id: rejectedId }),
        }
      );

      if (!res.ok) throw new Error("Failed to reject user");

      console.log("User rejected:", rejectedId);
      removeUserAndRefill(rejectedId);
    } catch (err) {
      console.error(err);
      alert("Something went wrong. Try again.");
    }
  };

  const handleConnectionRequest = async (requestedId: string) => {
    try {
      const res = await fetch(
        `http://localhost:8080/api/connections/${userId}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ requested_id: requestedId }),
        }
      );

      if (!res.ok) throw new Error("Failed to send connection request");

      console.log("Connection request sent to:", requestedId);
      removeUserAndRefill(requestedId);
    } catch (err) {
      console.error(err);
      alert("Something went wrong. Try again.");
    }
  };

  const handleHideUser = (id: string) => {
    removeUserAndRefill(id);
  };

  return (
    <div className="flex flex-col items-center pt-4 px-4 min-h-screen">
      {/* Logo */}
      <div>
        <AlienMeetLogo />
      </div>

      {/* Content row: always has Menu on left */}
      <div className="flex w-full max-w-7xl gap-2">
        {/* Left: Filter menu */}
        <div className="max-h-screen md:max-h-[80vh]">
          <Menu hasUnread={false}  />
        </div>

        {/* Right: dynamic content */}
        <div className="flex-1 flex items-center justify-center">
          {isLoading ? (
            <div className="text-center text-lg text-gray-400 font-serif">
              Loading...
            </div>
          ) : visibleUsers.length === 0 ? (
            <div className="text-center text-lg text-gray-400 font-serif">
              No matches found.
            </div>
          ) : (
            <div className="w-full pt-28 pl-4 md:pl-10">
              {/* Mobile: Horizontal scrolling */}
              <div className="md:hidden overflow-x-auto pb-4">
                <div className="flex gap-4 w-max">
                  {visibleUsers.map((user) => (
                    <div key={user.id} className="flex-shrink-0 w-80">
                      <FlipCard
                        front={
                          <MatchCardFront
                            profilepicSrc={user.profilepicSrc}
                            username={user.username}
                            age={user.age}
                            location={user.homeplanet}
                            lookingFor={user.lookingFor}
                            bio={user.bio ?? "..."}
                            cardColor={
                              lookingForColors[user.lookingFor] || "olive"
                            }
                            userId={user.id}
                            onReject={handleReject}
                            onConnectionRequest={handleConnectionRequest}
                            supermatch={user.supermatch}
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
                            cardColor={
                              lookingForColors[user.lookingFor] || "olive"
                            }
                            supermatch={user.supermatch}
                          />
                        }
                      />
                    </div>
                  ))}
                </div>
              </div>

              {/* Desktop: Grid layout */}
              <div className="hidden md:grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-8 justify-items-center">
                {visibleUsers.map((user) => (
                  <FlipCard
                    key={user.id}
                    front={
                      <MatchCardFront
                        profilepicSrc={user.profilepicSrc}
                        username={user.username}
                        age={user.age}
                        location={user.homeplanet}
                        lookingFor={user.lookingFor}
                        bio={user.bio ?? "..."}
                        cardColor={
                          lookingForColors[user.lookingFor] || "olive"
                        }
                        userId={user.id}
                        onReject={handleReject}
                        onConnectionRequest={handleConnectionRequest}
                        supermatch={user.supermatch}
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
                        cardColor={
                          lookingForColors[user.lookingFor] || "olive"
                        }
                        supermatch={user.supermatch}
                      />
                    }
                  />
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}