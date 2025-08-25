import { Filters } from "../../types";
import { useEffect, useState } from "react";


export function useUserSearch(userId: string, filters: Filters) {
    const [users, setUsers] = useState<any[]>([]);
    const [isLoading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);

        const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

        // Send filters to backend
        const requestBody = {
            lookingFor: Array.isArray(filters.lookingFor)
                ? filters.lookingFor[0] || "" // take first, or empty string, why is ana aaray at all?
                : filters.lookingFor,

            maxDistanceLy: filters.maxDistanceLy,

            bodyform: Array.isArray(filters.bodyform)
                ? filters.bodyform[0] || ""
                : filters.bodyform,

            interests: filters.interests,

            homeplanet: Array.isArray(filters.homeplanet)
                ? filters.homeplanet[0] || ""
                : filters.homeplanet,

            minAge: filters.minAge,

            maxAge: filters.maxAge,
        };

        fetch(`${API_URL}/api/matches/${userId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody),
        })
            .then((res) => {
                if (!res.ok) throw new Error("Failed request");
                return res.json();
            })
            .then((data) => setUsers(data))
            .catch((err) => console.error("Search failed", err))
            .finally(() => setLoading(false));
    }, [userId, filters]);

    return { users, isLoading };
}