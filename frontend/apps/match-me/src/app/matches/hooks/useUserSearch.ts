
import { useEffect, useState } from "react";
import {Filters, MatchUser } from "../types";


interface MatchResponse {
    currentUser: {
        id: string;
        username: string;
        age: number;
        homeplanet: string;
        lookingFor: string;
        bodyform: string;
        bio: string;
        interests: string[];
    };
    matches: MatchUser[]; // keep your existing MatchUser type
}

export function useUserSearch(userId: string, filters: Filters) {
    const [users, setUsers] = useState<MatchUser[]>([]);
    const [currentUser, setCurrentUser] = useState<MatchResponse["currentUser"] | null>(null);
    const [isLoading, setLoading] = useState(false);

    useEffect(() => {

        console.log("useUserSearch effect triggered with filters:", filters);

        setLoading(true);

        const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

        const normalize = (val: string | string[] | undefined | null) => {
            if (!val) return null;
            if (Array.isArray(val)) {
                return val.length > 0 && val[0].trim() !== "" ? val[0] : null;
            }
            return val.trim() !== "" ? val : null;
        };

        const requestBody = {
            lookingFor: normalize(filters.lookingFor),
            bodyform: normalize(filters.bodyform),
            homeplanet: normalize(filters.homeplanet),
            interests: Array.isArray(filters.interests) ? filters.interests : [],
            minAge: filters.minAge,
            maxAge: filters.maxAge,
            maxDistanceLy: filters.maxDistanceLy,
        };

        fetch(`${API_URL}/api/matches/${userId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody),
        })
            .then((res) => {
                if (!res.ok) throw new Error("Failed request");
                return res.json() as Promise<MatchResponse>;
            })
            .then((data) => {
                console.log("API Response - Matches count:", data.matches.length);
                console.log("Request body sent:", requestBody);
                console.log("Raw response from backend:", data);

                setCurrentUser(data.currentUser);

                setUsers(data.matches);
            })
            .catch((err) => console.error("Search failed", err))
            .finally(() => setLoading(false));
    }, [userId, filters]);

    return { users, currentUser, isLoading };
}