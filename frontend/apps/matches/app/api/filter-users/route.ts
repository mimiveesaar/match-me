import { NextResponse, userAgent } from "next/server";

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
    username: "Shelly",
    age: 25,
    location: "Venus",
    lookingFor: "Intergalactic Romance",
    bodyform: "Vaporous",
    bio: "Looking for someone to float with.",
    interests: ["hiking", "yoga"],
  },
  {
    id: "2",
    username: "Roberto",
    age: 30,
    location: "Mars",
    lookingFor: "Friendship",
    bodyform: "Gelatinous",
    bio: "Squishy and romantic.",
    interests: ["hiking", "puzzles", "cooking"],
  },
  {
    id: "3",
    username: "cosmic_poet",
    age: 45,
    location: "Mars",
    lookingFor: "Intergalactic Romance",
    bodyform: "Vaporous",
    bio: "Alien poet seeking cosmic muse.",
    interests: ["puzzles", "cooking"],
  },
  {
    id: "4",
    username: "astro_adventurer",
    age: 55,
    location: "Mars",
    lookingFor: "Travel Buddy",
    bodyform: "Gelatinous",
    bio: "defy gravity with me!",
    interests: ["puzzles", "hiking"],
  },
  {
    id: "5",
    username: "Clark",
    age: 62,
    location: "Mars",
    lookingFor: "Intergalactic Romance",
    bodyform: "Dexaspherical",
    bio: "SGE: Seeking Galactic Enlightenment. Join me on a journey through the stars.",
    interests: ["cooking", "movies", "music"],
  },
  {
    id: "6",
    username: "stellar_wanderer",
    age: 79,
    location: "Mars",
    lookingFor: "Friendship",
    bodyform: "Dexaspherical",
    bio: "Programmer by day, interstellar traveler by night. Let's code the universe together.",
    interests: ["hiking", "movies", "music"],
  },
  {
    id: "7",
    username: "cosmic_vibes",
    age: 87,
    location: "Mars",
    lookingFor: "Friendship",
    bodyform: "Phospopede",
    bio: "Serving cosmic vibes and good times. Let's explore the universe together.",
    interests: ["hiking", "movies", "music", "foraging mushrooms"],
  },
  {
    id: "8",
    username: "stellar_soul",
    age: 91,
    location: "Mars",
    lookingFor: "Friendship",
    bodyform: "Phospopede",
    bio: "gjellatinous and ready to mingle. Let's make some cosmic connections.",
    interests: ["hiking", "movies", "binary poetry", "foraging mushrooms"],
  },
  {
    id: "9",
    username: "Stacy",
    age: 106,
    location: "Mars",
    lookingFor: "Travel Buddy",
    bodyform: "Gelatinous",
    bio: "pkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkpkp",
    interests: ["movies", "binary poetry", "sheep counting", "foraging mushrooms"],
  },
  {
    id: "10",
    username: "Brady",
    age: 133,
    location: "Mars",
    lookingFor: "Travel Buddy",
    bodyform: "Vaporous",
    bio: "foraging mushrooms and exploring the cosmos. Let's connect over our shared interests.",
    interests: ["movies", "foraging mushrooms"],
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

    // looking for filter (if any lookingFor options selected)
    if (filters.lookingFor.length > 0 && !filters.lookingFor.includes(user.lookingFor)) {
      return false;
    }

    // interests filter (if any interests selected)
    if (filters.interests.length > 0) {
      const hasMatch = user.interests.some((interest: string) =>
        filters.interests.includes(interest)
      );
      if (!hasMatch) {
        return false;
      }
    }

    // age range filter
    if (
      filters.ageRange &&
      (user.age < filters.ageRange[0] || user.age > filters.ageRange[1])
    ) {
      return false;
    }

    // later you can add more filters here
    return true;
  });

  return NextResponse.json(filtered);
}