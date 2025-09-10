import { useEffect, useRef, useState } from 'react';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ChatMessage } from '@hooks/types';

export const useChat = (conversationId: string, userId: string, senderUsername: string) => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [otherUserTyping, setOtherUserTyping] = useState(false);

  const clientRef = useRef<Client | null>(null);
  const messagesSubRef = useRef<StompSubscription | null>(null);
  const typingSubRef = useRef<StompSubscription | null>(null);
  const typingTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    // Only connect WebSocket when we have a conversation
    if (!conversationId) {
      console.log('No conversation selected, skipping WebSocket connection');
      return;
    }

    console.log(`Connecting WebSocket for ${senderUsername} to conversation ${conversationId}`);

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      console.log(`WebSocket connected for ${senderUsername}`);

      // Subscribe to messages
      messagesSubRef.current = client.subscribe(
        `/topic/conversations/${conversationId}`,
        (msg: IMessage) => {
          const body = JSON.parse(msg.body) as ChatMessage;
          console.log('Received message:', body);

          if (body.type === 'MESSAGE') {
            setMessages((prev) => [...prev, body]);
          }
        });

      // Subscribe to typing events
      typingSubRef.current = client.subscribe(
        `/topic/conversations/${conversationId}/typing`,
        (msg: IMessage) => {
          const body = JSON.parse(msg.body) as ChatMessage;
          if (body.senderId !== userId) {
            console.log('Typing event received:', body.typing);
            setOtherUserTyping(body.typing ?? false);
          }
        }
      );
    };

    client.activate();
    clientRef.current = client;

    return () => {
      console.log(`Disconnecting WebSocket for ${senderUsername}`);
      messagesSubRef.current?.unsubscribe();
      typingSubRef.current?.unsubscribe();
      if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);
      client.deactivate();
    };
  }, [conversationId, userId, senderUsername]); // Added senderUsername to deps

  const sendMessage = (content: string) => {
    if (!clientRef.current || !clientRef.current.connected) {
      console.error('Cannot send message: WebSocket not connected');
      return;
    }

    const message: ChatMessage = {
      conversationId,
      senderId: userId,
      senderUsername,
      content,
      type: 'MESSAGE',
      timestamp: [],
    };

    console.log('Sending message:', message);

    clientRef.current.publish({
      destination: `/app/chat/${conversationId}/sendMessage`,
      body: JSON.stringify(message),
    });
  };

  const sendTyping = (isTyping: boolean) => {
    if (!clientRef.current || !clientRef.current.connected) return;

    const typingMessage: ChatMessage = {
      conversationId,
      senderId: userId,
      senderUsername,
      content: '',
      type: 'TYPING',
      timestamp: [],
      typing: isTyping,
    };

    console.log(`Sending typing event: ${isTyping}`);

    clientRef.current.publish({
      destination: `/app/chat/${conversationId}/typing`,
      body: JSON.stringify(typingMessage),
    });
  };

  const handleTyping = () => {
    sendTyping(true);

    if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);

    typingTimeoutRef.current = setTimeout(() => {
      sendTyping(false);
    }, 1000);
  };

  return { messages, setMessages, sendMessage, handleTyping, otherUserTyping };
};