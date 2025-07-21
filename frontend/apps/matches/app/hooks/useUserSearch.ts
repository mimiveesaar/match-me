import { useEffect, useState } from "react";

export interface Filters {
    ageRange: [number, number];
    distanceRange: [number, number];
    bodyform: string[];
    interests: string[];
    lookingFor: string[];
}

export function useUserSearch(filters: Filters) {
    const [users, setUsers] = useState([]);
    const [isLoading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);

        fetch("/api/filter-users", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(filters),
        })
            .then((res) => res.json())
            .then((data) => setUsers(data))
            .catch((err) => console.error("Search failed", err))
            .finally(() => setLoading(false));
    }, [filters]);

    return { users, isLoading };
}