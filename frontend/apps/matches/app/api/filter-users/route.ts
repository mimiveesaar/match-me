import { NextResponse } from "next/server";

// Define the expected filter type (optional but helps)
type Filters = {
  ageRange: [number, number];
  distanceRange: [number, number];
  bodyform: string[];
  interests: string[];
  lookingFor: string[];
};

export async function POST(request: Request) {
  const filters: Filters = await request.json();

  console.log("Received filters:", filters);

  // 🔁 This is where you'd filter real data from your database
  // For now, we return dummy users
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

  return NextResponse.json(mockUsers);
}