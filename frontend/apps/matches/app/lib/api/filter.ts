export async function postFilterCriteria(criteria: {
  bodyform: string[];
}) {
  try {
    const res = await fetch("/api/filter-users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(criteria),
    });

    if (!res.ok) throw new Error("Failed to fetch filtered users");
    return await res.json();
  } catch (error) {
    console.error("POST request failed:", error);
    throw error;
  }
}