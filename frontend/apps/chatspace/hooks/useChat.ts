import { useEffect, useRef, useState } from 'react';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ChatMessage } from '@hooks/types';

export const useChat = (conversationId: string, userId: string) => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [otherUserTyping, setOtherUserTyping] = useState(false);

  const clientRef = useRef<Client | null>(null);
  const messagesSubRef = useRef<StompSubscription | null>(null);
  const typingSubRef = useRef<StompSubscription | null>(null);
  const typingTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      console.log('Connected to WebSocket');

      // Subscribe to messages
      messagesSubRef.current = client.subscribe(
        `/topic/conversations/${conversationId}`,
        (msg: IMessage) => {
          const body = JSON.parse(msg.body) as ChatMessage;
          console.log('📥 Received message from backend:', body);
          
          if (body.type === 'MESSAGE') {
            setMessages((prev) => [...prev, body]);
          }
        });

      // Subscribe to typing events
      typingSubRef.current = client.subscribe('/topic/typing', (msg: IMessage) => {
        const body = JSON.parse(msg.body) as ChatMessage;
        if (body.conversationId === conversationId && body.senderId !== userId) {
          console.log('📥 typing received in other tab', body);
          setOtherUserTyping(body.typing ?? false);
        }
      });
    };

    client.activate();
    clientRef.current = client;

    return () => {
      messagesSubRef.current?.unsubscribe();
      if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);
      client.deactivate();
    };
  }, [conversationId, userId]);

  const sendMessage = (content: string) => {
    if (!clientRef.current || !clientRef.current.connected) return;

    const message: ChatMessage = {
      conversationId,
      senderId: userId,
      senderUsername: 'Henry',
      content,
      type: 'MESSAGE',
      timestamp: [],
    };

    console.log('📤 Sending message to backend:', message);

    clientRef.current.publish({
      destination: `/app/chat/${conversationId}/sendMessage`, // <-- updated
      body: JSON.stringify(message),
    });
  };

  const sendTyping = (isTyping: boolean) => {
    if (!clientRef.current || !clientRef.current.connected) return;

    const typingMessage: ChatMessage = {
      conversationId,
      senderId: userId,
      senderUsername: 'Henry',
      content: '',
      type: 'TYPING',
      timestamp: [],
      typing: isTyping,
    };

    console.log('Sending typing event', isTyping);

    clientRef.current.publish({
      destination: '/app/chat.typing',
      body: JSON.stringify(typingMessage),
    });
  };

  // Call this on every keystroke
  const handleTyping = () => {
    sendTyping(true);

    if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);

    typingTimeoutRef.current = setTimeout(() => {
      sendTyping(false);
    }, 1000); // stop typing after 1s of inactivity
  };

  return { messages, setMessages, sendMessage, handleTyping, otherUserTyping };
};