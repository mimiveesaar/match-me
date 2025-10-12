"use client";

import { Connections } from "components/templates";

export default function ConnectionsPage() {

    const incomingPings = [
        {
            id: "1234",
            username: "Kati",
            profilePictureUrl: "https://example.com/profile.jpg",
        },
    ];

    const outgoingPings = [
        {
            id: "4321",
            username: "Mimi",
            profilePictureUrl: "https://example.com/profile.jpg",
        }
    ]

    const myConnections = [
        {
            id: "4444",
            username: "Rivo",
            profilePictureUrl: "https://example.com/profile.jpg",

        }
    ]

    return (
        <div>
            <Connections incomingPings={{incomingPings}} outgoingPings={{outgoingPings}} myConnections={{myConnections}}></Connections>
        </div>
    );
}