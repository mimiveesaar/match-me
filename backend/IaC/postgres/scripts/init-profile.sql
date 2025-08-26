
CREATE USER "profile" WITH PASSWORD 'profile';
CREATE DATABASE "profile" WITH OWNER "profile";
GRANT ALL PRIVILEGES ON DATABASE "profile" TO "profile";

CREATE TABLE table_name(  
    id int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    create_time DATE,
    name VARCHAR(255)
);
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

-- Create the interests table
CREATE TABLE IF NOT EXISTS interests (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
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



-- Seed interests data
INSERT INTO interests (name) VALUES ('Music') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Art') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Sports') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Travel') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Reading') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Gaming') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Cooking') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Photography') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Dancing') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Writing') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Yoga') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Hiking') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Meditation') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Movies') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Technology') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Science') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Politics') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Animals') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Fashion') ON CONFLICT DO NOTHING;
INSERT INTO interests (name) VALUES ('Gardening') ON CONFLICT DO NOTHING;