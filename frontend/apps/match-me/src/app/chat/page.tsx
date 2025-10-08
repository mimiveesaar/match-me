'use client';

import { ConnectionsMenu } from 'components/organisms';
import { ChatWindow } from 'components/organisms';
import { MessageInput } from 'components/organisms';
import { useChat } from './hooks/useChat';
import { useEffect, useMemo, useState } from 'react';
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

function Chatspace({ userId }: { userId: string }) {

    useEffect(() => {
        document.title = 'Chatspace | alien.meet'
    }, [])

    const [users, setUsers] = useState<
        Array<{ id: string; username: string; src?: string; isOnline: boolean }>
    >([]);
    const [conversationId, setConversationId] = useState<string | null>(null);
    const { setHasUnread, subscribeToUnread, hasUnread, isConnected } = useWebSocket();
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
        profilePicSrc: string;
    }

    useEffect(() => {
        async function fetchConnectionsAndConversations() {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/users/${userId}/connections`,
                );

                console.log("[Frontend] Raw API response:", response.data);
                response.data.forEach((user: UserConnection) => {
                    console.log(`  - ${user.username}: profilePicSrc = ${user.profilePicSrc}`);
                });

                const connections = response.data.map((user: UserConnection) => ({
                    id: user.id,
                    username: user.username,
                    src: user.profilePicSrc,
                }));

                try {
                    const onlineRes = await axios.get(`http://localhost:8080/api/presence/online-users`);
                    const onlineUserIds: string[] = onlineRes.data;
                    const connections = (response.data as UserConnection[]).map((user) => ({
                        id: user.id,
                        username: user.username,
                        src: user.profilePicSrc,
                    }));
                    const enrichedConnections = connections.map((user) => ({
                        ...user,
                        isOnline: onlineUserIds.includes(user.id),
                    }));

                    console.log("Enriched users:", enrichedConnections);
                    setUsers(enrichedConnections);
                } catch (err) {
                    console.error("Failed to fetch online users", err);
                    setUsers(connections); // fallback if presence API fails
                }

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

    const { onlineUsers, isUserOnline } = useWebSocket();

    const enrichedUsers = useMemo(() => {
        console.log("Users array:", users);
        return users.map((u) => ({
            ...u,
            isOnline: isUserOnline(u.id),
        }));
    }, [users, onlineUsers]);

    useEffect(() => {
        console.log("Enriched users:", enrichedUsers);
    }, [enrichedUsers]);

    return (
        <div className="flex flex-col min-h-screen bg-ivory">


            <main className="flex w-full max-w-7xl mx-auto">


                <section className="flex flex-col flex-1 relative">
                    <div className="flex-1 mt-24">
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

                <aside className="w-72 mt-24 flex flex-col">
                    <ConnectionsMenu
                        users={enrichedUsers}
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
