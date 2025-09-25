import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let client: Client | null = null;

export const getStompClient = (userId: string): Client => {
    if (client) return client;

    console.log("[STOMP INIT] Creating singleton client with userId:", userId);

    client = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
        reconnectDelay: 5000,
        connectHeaders: {
            userId,
        },
        debug: (str) => console.log(`[STOMP DEBUG] ${str}`),
    });

    client.activate(); // Activate only once

    return client;
};
