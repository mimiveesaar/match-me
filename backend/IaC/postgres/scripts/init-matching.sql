CREATE USER "matching" WITH PASSWORD 'matching';
CREATE DATABASE "matching" WITH OWNER "matching";
GRANT ALL PRIVILEGES ON DATABASE "matching" TO "matching";

-- CREATE TABLE users (
--   id SERIAL PRIMARY KEY,
--   username TEXT NOT NULL UNIQUE,
--   home_planet TEXT NOT NULL,
--   looking_for TEXT NOT NULL,
--   bio TEXT,
--   image_url TEXT,
--   interests TEXT[],         -- array of interest keywords
--   age INTEGER,
--   bodyform TEXT,
--   created_at TIMESTAMP DEFAULT NOW()
-- );

-- INSERT INTO users (
--   username, home_planet, relationship_type, bio, image_url, interests, age, bodyform
-- ) VALUES
--   ('zoorgle', 'Mars', 'Martian lover', 'Dreamy space-romancer.', 'https://example.com/zoorgle.png', ARRAY['hoverboarding', 'telepathy'], 120, 'Gelatinous'),
--   ('nari-9', 'Venus', 'Plasma soulmate', 'We ride lightwaves.', 'https://example.com/nari-9.png', ARRAY['cloud surfing', 'volcano climbing'], 87, 'Vaporous'),
--   ('krelth', 'Jupiter', 'Cuddle beast', 'Gentle giant from the storm.', 'https://example.com/krelth.png', ARRAY['storm chasing', 'hug wrestling'], 300, 'Colossal'),
--   ('elixi', 'Mercury', 'Sun dancer', 'Fast and fiery.', 'https://example.com/elixi.png', ARRAY['sunbathing', 'racing'], 35, 'Liquid metal'),
--   ('blorp', 'Neptune', 'Mind-meld mate', 'Quiet but deep.', 'https://example.com/blorp.png', ARRAY['deep thinking', 'bubble jazz'], 250, 'Amorphous');