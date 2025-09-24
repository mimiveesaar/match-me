import { Client, StompSubscription, IMessage } from "@stomp/stompjs";
import { useState, useRef, useEffect } from "react";
import SockJS from "sockjs-client";

export const usePresence = (userId: string) => {
  const [status, setStatus] = useState<'ONLINE' | 'OFFLINE'>('OFFLINE');
  const clientRef = useRef<Client | null>(null);
  const subRef = useRef<StompSubscription | null>(null);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      subRef.current = client.subscribe(`/topic/status/${userId}`, (msg: IMessage) => {
        setStatus(msg.body as 'ONLINE' | 'OFFLINE');
        console.log(`User ${userId} is now ${msg.body}`);
      });
    };

    client.activate();
    clientRef.current = client;

    return () => {
      subRef.current?.unsubscribe();
      client.deactivate();
    };
  }, [userId]);

  return { status };
};