import { useEffect, useState } from "react";
import { useWebSocket } from "../utils/WebSocketContext";


export const usePresence = (userId: string) => {
    const { isUserOnline, isConnected } = useWebSocket();
    const [status, setStatus] = useState<'ONLINE' | 'OFFLINE'>('OFFLINE');

    useEffect(() => {
        if (!userId || !isConnected) return;

        setStatus(isUserOnline(userId) ? 'ONLINE' : 'OFFLINE');
    }, [userId, isConnected, isUserOnline]);

    return { status };
};
