"use client";

import { Connections } from "components/templates";
import { useEffect, useState } from "react";


export default function ConnectionsPage() {
    const [myConnections, setMyConnections] = useState([]);
    const [incomingPings, setIncomingPings] = useState([]);
    const [outgoingPings, setOutgoingPings] = useState([]);

    const authToken = "eyJhbGciOiJIUzI1NiIsImFhYSI6dHJ1ZX0.eyJpc3MiOiJtYXRjaC1tZSIsInVzZXJJZCI6IjExMTExMTExLTExMTEtMTExMS0xMTExLTExMTExMTExMTExMSIsIlVzZXJTdGF0dXNDb2RlIjoxLCJpYXQiOjE3NjA0Mjk1MDYsImV4cCI6Mjc2MDQzMDEwNn0.nmqTIiR1xiWCam1WhtcfXCRKCpuKdIxjyt1-5CY9G-Y";


    useEffect(() => {
        async function fetchAcceptedConnections() {
            try {
                const response = await fetch("/api/v1/connections/accepted", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${authToken}`,
                    },
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch accepted connections");
                }

                const data = await response.json();
                setMyConnections(data.accepted_connections || []);
            } catch (error) {
                console.error("Error fetching accepted connections:", error);
            }
        }

        fetchAcceptedConnections();
    }, [authToken]);


    useEffect(() => {
        async function fetchPendingConnections() {
            try {
                const response = await fetch("/api/v1/connections/pending-requests", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${authToken}`,
                    },
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch pending connections");
                }

                const data = await response.json();
                setIncomingPings(data.incoming_requests || []);
                setOutgoingPings(data.outgoing_requests || []);
            } catch (error) {
                console.error("Error fetching pending connections:", error);
            }
        }

        fetchPendingConnections();
    }, [authToken]);

    return (
        <div>
            <Connections
                incomingPings={{incomingPings}}
                outgoingPings={{outgoingPings}}
                myConnections={{myConnections}}
            />
        </div>
    );
}
