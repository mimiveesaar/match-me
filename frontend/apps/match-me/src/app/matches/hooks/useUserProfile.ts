import { useEffect, useState } from "react";

export function useUserProfile(userId: string) {
  const [user, setUser] = useState<any | null>(null);
  const [isLoading, setLoading] = useState(false);

  useEffect(() => {
    if (!userId) return;

    setLoading(true);
    const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

    fetch(`${API_URL}/api/users/${userId}`)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch user profile");
        return res.json();
      })
      .then((data) => {
        setUser(data);
      })
      .catch((err) => console.error("User profile fetch failed", err))
      .finally(() => setLoading(false));
  }, [userId]);

  return { user, isLoading };
}