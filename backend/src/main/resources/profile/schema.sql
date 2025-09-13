-- Drop and recreate tables with proper SERIAL setup
DROP TABLE IF EXISTS interests CASCADE;
DROP TABLE IF EXISTS bodyforms CASCADE;
DROP TABLE IF EXISTS homeplanets CASCADE;
DROP TABLE IF EXISTS looking_for CASCADE;

-- Create tables with proper SERIAL PRIMARY KEY
CREATE TABLE bodyforms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE homeplanets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE looking_for (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE interests (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);