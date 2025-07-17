import { NextResponse } from "next/server";

type Filters = {
  ageRange: [number, number];
  distanceRange: [number, number];
  bodyform: string[];
  interests: string[];
  lookingFor: string[];
};

const mockUsers = [
  {
    id: "1",
    location: "Venus",
    relationshipType: "Plasma soulmate",
    bodyform: "Vaporous",
    bio: "Looking for someone to float with.",
  },
  {
    id: "2",
    location: "Mars",
    relationshipType: "Martian lover",
    bodyform: "Gelatinous",
    bio: "Squishy and romantic.",
  },
];

export async function POST(request: Request) {
  const filters: Filters = await request.json();
  console.log("Received filters:", filters);

  const filtered = mockUsers.filter((user) => {
    // bodyform filter (if any bodyforms selected)
    if (filters.bodyform.length > 0 && !filters.bodyform.includes(user.bodyform)) {
      return false;
    }

    // later you can add more filters here
    return true;
  });

  return NextResponse.json(filtered);
}