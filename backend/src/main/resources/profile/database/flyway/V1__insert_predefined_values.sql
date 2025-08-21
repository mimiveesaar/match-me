-- Create tables if not exist
CREATE TABLE IF NOT EXISTS bodyforms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS homeplanets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS looking_for (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Seed data
INSERT INTO bodyforms (name) VALUES ('Humanoid') ON CONFLICT (name) DO NOTHING;
INSERT INTO bodyforms (name) VALUES ('Reptilian') ON CONFLICT (name) DO NOTHING;
INSERT INTO bodyforms (name) VALUES ('Energy Being') ON CONFLICT (name) DO NOTHING;

INSERT INTO homeplanets (name) VALUES ('Earth') ON CONFLICT (name) DO NOTHING;
INSERT INTO homeplanets (name) VALUES ('Nibiru') ON CONFLICT (name) DO NOTHING;
INSERT INTO homeplanets (name) VALUES ('Zog Prime') ON CONFLICT (name) DO NOTHING;

INSERT INTO looking_for (name) VALUES ('Friendship') ON CONFLICT (name) DO NOTHING;
INSERT INTO looking_for (name) VALUES ('Romance') ON CONFLICT (name) DO NOTHING;
INSERT INTO looking_for (name) VALUES ('Alliance') ON CONFLICT (name) DO NOTHING;
