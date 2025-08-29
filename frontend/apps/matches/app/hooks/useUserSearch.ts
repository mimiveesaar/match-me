import { Filters } from "../../types";
import { useEffect, useState } from "react";


export function useUserSearch(userId: string, filters: Filters) {
    const [users, setUsers] = useState<any[]>([]);
    const [isLoading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);

        const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

        // Send filters to backend
        const normalize = (val: string | string[] | undefined | null) => {
            if (!val) return null; // catches null/undefined/""
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
                return res.json();
            })
            .then((data) => {
                console.log("Raw response from backend:", data);
                setUsers(data);
            })
            .catch((err) => console.error("Search failed", err))
            .finally(() => setLoading(false));
    }, [userId, filters]);

    return { users, isLoading };
}