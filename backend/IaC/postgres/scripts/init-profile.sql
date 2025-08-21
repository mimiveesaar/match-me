CREATE USER "profile" WITH PASSWORD 'profile';
CREATE DATABASE "profile" WITH OWNER "profile";
GRANT ALL PRIVILEGES ON DATABASE "profile" TO "profile";

-- Create tables
CREATE TABLE IF NOT EXISTS bodyforms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS homeplanets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS looking_for (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Seed data
INSERT INTO bodyforms (name) VALUES ('Humanoid') ON CONFLICT DO NOTHING;
INSERT INTO bodyforms (name) VALUES ('Reptilian') ON CONFLICT DO NOTHING;
INSERT INTO bodyforms (name) VALUES ('Energy Being') ON CONFLICT DO NOTHING;

INSERT INTO homeplanets (name) VALUES ('Earth') ON CONFLICT DO NOTHING;
INSERT INTO homeplanets (name) VALUES ('Nibiru') ON CONFLICT DO NOTHING;
INSERT INTO homeplanets (name) VALUES ('Zog Prime') ON CONFLICT DO NOTHING;

INSERT INTO looking_for (name) VALUES ('Friendship') ON CONFLICT DO NOTHING;
INSERT INTO looking_for (name) VALUES ('Romance') ON CONFLICT DO NOTHING;
INSERT INTO looking_for (name) VALUES ('Alliance') ON CONFLICT DO NOTHING;
