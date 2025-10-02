"use client";

import { useEffect, useRef, useState } from 'react';
import { IMessage, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from './types';
import { useWebSocket } from '../utils/WebSocketContext';

export const useChat = (
    conversationId: string,
    userId: string,
    senderUsername: string
) => {
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const { client, typingState, setTypingState, isConnected } = useWebSocket();
    const messagesSubRef = useRef<StompSubscription | null>(null);
    const typingSubRef = useRef<StompSubscription | null>(null);
    const typingTimeoutRef = useRef<NodeJS.Timeout | null>(null);

    // Derived state: is the other user typing?
    const otherUserTyping = Object.entries(typingState[conversationId] || {}).some(
        ([uid, isTyping]) => uid !== userId && isTyping
    );

    useEffect(() => {
        if (!conversationId || !client || !isConnected) return;

        console.log(`🔌 useChat: Subscribing to conversation ${conversationId}`);

        // Subscribe to incoming messages
        messagesSubRef.current = client.subscribe(
            `/topic/conversations/${conversationId}`,
            (msg: IMessage) => {
                const body = JSON.parse(msg.body) as ChatMessage;
                console.log('[useChat] Received message:', body);

                if (body.type === 'MESSAGE') {
                    setMessages((prev) => [...prev, body]);
                }
            }
        );

        // Subscribe to typing updates
        typingSubRef.current = client.subscribe(
            `/topic/conversations/${conversationId}/typing`,
            (msg: IMessage) => {
                const body = JSON.parse(msg.body) as ChatMessage;
                const sender = body.senderId;
                const isTyping = body.typing ?? false;

                setTypingState((prev) => ({
                    ...prev,
                    [conversationId]: {
                        ...(prev[conversationId] || {}),
                        [sender]: isTyping,
                    },
                }));
            }
        );

        return () => {
            console.log(`🛑 useChat: Unsubscribing from conversation ${conversationId}`);
            messagesSubRef.current?.unsubscribe();
            typingSubRef.current?.unsubscribe();
            if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);
        };
    }, [conversationId, client, isConnected]);

    const sendMessage = (content: string) => {
        if (!client || !isConnected) {
            console.error('[useChat] Cannot send message: client not connected');
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

        console.log('[useChat] Sending message:', message);

        client.publish({
            destination: `/app/chat`,
            body: JSON.stringify(message),
        });
    };

    const sendTyping = (isTyping: boolean) => {
        if (!client || !isConnected) return;

        const typingMessage: ChatMessage = {
            conversationId,
            senderId: userId,
            senderUsername,
            content: '',
            type: 'TYPING',
            timestamp: [],
            typing: isTyping,
        };

        client.publish({
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

    return {
        messages,
        setMessages,
        sendMessage,
        handleTyping,
        otherUserTyping,
    };
};
