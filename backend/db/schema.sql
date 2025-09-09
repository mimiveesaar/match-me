CREATE TABLE users (
    id UUID PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20),
    last_active TIMESTAMP
);

CREATE TABLE conversations (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE conversation_participants (
    conversation_id UUID NOT NULL REFERENCES conversations(id),
    user_id UUID NOT NULL REFERENCES users(id),
    PRIMARY KEY (conversation_id, user_id)
);

CREATE TABLE messages (
    id UUID PRIMARY KEY,
    conversation_id UUID NOT NULL REFERENCES conversations(id),
    sender_id UUID NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20)
);

-- Users
INSERT INTO users (id, password, status, last_active) VALUES
('11111111-1111-1111-1111-111111111111', 'password123', 'online', NOW() - INTERVAL '10 minutes'),
('22222222-2222-2222-2222-222222222222', 'password456', 'offline', NOW() - INTERVAL '2 hours'),
('33333333-3333-3333-3333-333333333333', 'password789', 'away', NOW() - INTERVAL '30 minutes');

-- Conversations
INSERT INTO conversations (id, created_at) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', NOW() - INTERVAL '1 day'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', NOW() - INTERVAL '2 days');

-- Conversation Participants
INSERT INTO conversation_participants (conversation_id, user_id) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333');

-- Messages
INSERT INTO messages (id, conversation_id, sender_id, content, timestamp, status) VALUES
('aaaa1111-1111-1111-1111-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'Hey, how are you?', NOW() - INTERVAL '9 minutes', 'sent'),
('aaaa2222-2222-2222-2222-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222', 'I am good, thanks! You?', NOW() - INTERVAL '8 minutes', 'delivered'),
('bbbb1111-1111-1111-1111-bbbbbbbbbbbb', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 'Hello, ready for the meeting?', NOW() - INTERVAL '1 day', 'read'),
('bbbb2222-2222-2222-2222-bbbbbbbbbbbb', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333', 'Yes, I am joining now.', NOW() - INTERVAL '23 hours', 'read');