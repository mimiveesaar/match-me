'use client';

import { AlienMeetLogo } from 'components';
import { Menu } from 'components/organisms';
import { ConnectionsMenu } from 'components/organisms';
import { ChatWindow } from 'components/organisms';
import { MessageInput } from 'components/organisms';
import { useChat } from './hooks/useChat';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { useWebSocket, WebSocketProvider } from './utils/WebSocketContext';

export default function ChatspaceWrapper() {
    const search =
        typeof window !== 'undefined' ? window.location.search.toLowerCase() : '';
    const userId = search.includes('userb')
        ? '22222222-2222-2222-2222-222222222222'
        : '11111111-1111-1111-1111-111111111111';

    return (
        <WebSocketProvider userId={userId}>
            <Chatspace userId={userId} />
        </WebSocketProvider>
    );
}

// Separated inner component to safely use context
function Chatspace({ userId }: { userId: string }) {
    const [users, setUsers] = useState<
        Array<{ id: string; username: string; isOnline: boolean }>
    >([]);
    const [conversationId, setConversationId] = useState<string | null>(null);
    const { setHasUnread, subscribeToUnread, hasUnread, isConnected } = useWebSocket(); // ✅ now safe
    const senderUsername = userId === '22222222-2222-2222-2222-222222222222'
        ? 'bob_cosmic'
        : 'alice_space';

    const { messages, setMessages, sendMessage, handleTyping, otherUserTyping } =
        useChat(conversationId ?? '', userId, senderUsername);

    const [input, setInput] = useState('');

    interface UserConnection {
        id: string;
        username: string;
        status: string;
    }

    useEffect(() => {
        async function fetchConnectionsAndConversations() {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/users/${userId}/connections`,
                );

                const connections = response.data.map((user: UserConnection) => ({
                    id: user.id,
                    username: user.username,
                }));

                setUsers(connections); // ✅ Only save ID + username, no isOnline here

                const convosRes = await axios.get(
                    `http://localhost:8080/api/conversations/user/${userId}`,
                );

                const conversations = convosRes.data;

                if (conversations.length > 0) {
                    const latestConvo = conversations[0];
                    setConversationId(latestConvo.id);
                    setMessages(latestConvo.messages || []);
                }
            } catch (err) {
                console.error('Failed to fetch connections or conversations:', err);

                const mockConnections =
                    userId === '11111111-1111-1111-1111-111111111111'
                        ? [
                            {
                                id: '22222222-2222-2222-2222-222222222222',
                                username: 'bob_cosmic',
                            },
                        ]
                        : [
                            {
                                id: '11111111-1111-1111-1111-111111111111',
                                username: 'alice_space',
                            },
                        ];
                setUsers(mockConnections);
            }
        }

        fetchConnectionsAndConversations();
    }, [userId]);


    useEffect(() => {
        if (!conversationId || !userId) return;

        const markAsRead = async () => {
            try {
                await axios.put(
                    `http://localhost:8080/api/conversations/${conversationId}/read`,
                    null,
                    { params: { userId } },
                );

                setMessages((prev) =>
                    prev.map((m) =>
                        m.senderId !== userId ? { ...m, status: 'READ' } : m,
                    ),
                );

                setHasUnread(false);
            } catch (err) {
                console.error('Failed to mark conversation as read', err);
            }
        };

        markAsRead();
    }, [conversationId, userId]);

    useEffect(() => {
        if (!userId || !conversationId || !isConnected) return;

        const sub = subscribeToUnread(userId, conversationId);

        return () => {
            sub.unsubscribe();
        };
    }, [userId, conversationId, isConnected]);

    return (
        <div className="flex flex-col h-screen bg-ivory">
            <header className="flex justify-center py-4 flex-shrink-0">
                <AlienMeetLogo />
            </header>

            <main className="flex w-full max-w-7xl mx-auto">
                <aside className="w-80 flex items-center justify-center mt-20">
                    <Menu hasUnread={hasUnread} />
                </aside>

                <section className="flex flex-col flex-1 relative">
                    <div className="flex-1 mt-32">
                        <ChatWindow
                            messages={messages}
                            currentUserId={userId}
                            otherUserTyping={otherUserTyping}
                        />
                    </div>

                    <div className="p-4 flex-shrink-0">
                        <MessageInput
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            onSend={(msg) => {
                                sendMessage(msg);
                                setInput('');
                            }}
                            onTyping={handleTyping}
                        />
                    </div>
                </section>

                <aside className="w-72 mt-32 flex flex-col">
                    <ConnectionsMenu
                        users={users}
                        onSelectUser={async (otherUserId) => {
                            try {
                                const response = await axios.post(
                                    `http://localhost:8080/api/conversations/open`,
                                    null,
                                    {
                                        params: { userId, otherUserId },
                                    },
                                );
                                const convo = response.data;
                                setConversationId(convo.id);
                                setMessages(convo.messages || []);
                            } catch (err) {
                                console.error('Failed to open conversation:', err);
                            }
                        }}
                    />
                </aside>
            </main>
        </div>
    );
}
