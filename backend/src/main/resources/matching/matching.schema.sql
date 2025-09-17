CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    age INT,
    profilepic_src VARCHAR(255),
    homeplanet_id INT,
    looking_for_id INT,
    bodyform_id INT,
    bio TEXT
);

CREATE TABLE IF NOT EXISTS user_rejections (
    id UUID PRIMARY KEY,
    rejecter_id UUID NOT NULL,
    rejected_id UUID NOT NULL,
    CONSTRAINT fk_rejecter FOREIGN KEY (rejecter_id) REFERENCES users(id),
    CONSTRAINT fk_rejected FOREIGN KEY (rejected_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS user_interests (
    user_id UUID NOT NULL,
    interest_id INT NOT NULL,
    PRIMARY KEY (user_id, interest_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_interest FOREIGN KEY (interest_id) REFERENCES interests(id)
);

CREATE TABLE IF NOT EXISTS looking_for (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS interests (
    id INT PRIMARY KEY,
    interest VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS homeplanets (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS connection_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id UUID NOT NULL,
    requested_id UUID NOT NULL,
    CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_requested FOREIGN KEY (requested_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_request UNIQUE (requester_id, requested_id)
);

CREATE TABLE IF NOT EXISTS bodyforms (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);
