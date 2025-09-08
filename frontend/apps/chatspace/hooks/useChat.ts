'use client';

import { useEffect, useRef, useState } from 'react';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ChatMessage } from './types';


export const useChat = (conversationId: string, userId: string) => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const clientRef = useRef<Client | null>(null);
  const subscriptionRef = useRef<StompSubscription | null>(null);

  useEffect(() => {
    // Create STOMP client with SockJS
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      console.log('Connected to WebSocket');

      // Subscribe to messages for this conversation
      subscriptionRef.current = client.subscribe('/topic/messages', (msg: IMessage) => {
        const body = JSON.parse(msg.body) as ChatMessage;
        if (body.conversationId === conversationId) {
          setMessages((prev) => [...prev, body]);
        }
      });
    };

    client.activate();
    clientRef.current = client;

    return () => {
      subscriptionRef.current?.unsubscribe();
      client.deactivate();
    };
  }, [conversationId]);

  const sendMessage = (content: string) => {
    if (!clientRef.current || !clientRef.current.connected) return;

    const message: ChatMessage = {
        conversationId,
        senderId: userId,
        content,
        type: 'MESSAGE',
        timestamp: ''
    };

    clientRef.current.publish({
      destination: '/app/chat.sendMessage',
      body: JSON.stringify(message),
    });
  };

  return { messages, sendMessage };
};