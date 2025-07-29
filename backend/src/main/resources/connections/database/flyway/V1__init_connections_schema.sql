CREATE SCHEMA IF NOT EXISTS connections;

CREATE TABLE IF NOT EXISTS connections.pending_connections (
    id UUID PRIMARY KEY,
    sender_user_id UUID NOT NULL,
    target_user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_pending_connection_sender_id ON connections.pending_connections(sender_user_id);
CREATE INDEX idx_pending_connection_target_id ON connections.pending_connections(target_user_id);
CREATE UNIQUE INDEX unique_pending_pair_any_direction
    ON connections.pending_connections (
        LEAST(sender_user_id, target_user_id),
        GREATEST(sender_user_id, target_user_id)
    );

CREATE TABLE IF NOT EXISTS connections.accepted_connections (
    id UUID PRIMARY KEY,
    sender_user_id UUID NOT NULL,
    target_user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE UNIQUE INDEX unique_accepted_pair_any_direction
    ON connections.accepted_connections (
        LEAST(sender_user_id, target_user_id),
        GREATEST(sender_user_id, target_user_id)
    );

CREATE INDEX idx_accepted_connection_sender_id ON connections.accepted_connections(sender_user_id);
CREATE INDEX idx_accepted_connection_target_id ON connections.accepted_connections(target_user_id);

CREATE TABLE IF NOT EXISTS connections.rejected_connections (
    id UUID PRIMARY KEY,
    sender_user_id UUID NOT NULL,
    target_user_id UUID NOT NULL,
    rejected_by_user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_rejected_connection_sender_id ON connections.rejected_connections(sender_user_id);
CREATE INDEX idx_rejected_connection_target_id ON connections.rejected_connections(target_user_id);
CREATE INDEX idx_rejected_connection_rejected_by_id ON connections.rejected_connections(rejected_by_user_id);

CREATE UNIQUE INDEX unique_rejected_pair_any_direction
    ON connections.rejected_connections (
        LEAST(sender_user_id, target_user_id),
        GREATEST(sender_user_id, target_user_id)
    );

CREATE TABLE IF NOT EXISTS connections.blocked_connections (
    id UUID PRIMARY KEY,
    blocker_user_id UUID NOT NULL,
    blocked_user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_blocked_connection_blocker_id ON connections.blocked_connections(blocker_user_id);
CREATE INDEX idx_blocked_connection_blocked_id ON connections.blocked_connections(blocked_user_id);

CREATE UNIQUE INDEX unique_blocked_pair_any_direction
    ON connections.blocked_connections (
        LEAST(blocker_user_id, blocked_user_id),
        GREATEST(blocker_user_id, blocked_user_id)
    );
