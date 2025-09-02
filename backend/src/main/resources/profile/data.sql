-- Seed bodyforms
INSERT INTO bodyforms (name) VALUES ('Humanoid') ON CONFLICT (name) DO NOTHING;
INSERT INTO bodyforms (name) VALUES ('Reptilian') ON CONFLICT (name) DO NOTHING;
INSERT INTO bodyforms (name) VALUES ('Energy Being') ON CONFLICT (name) DO NOTHING;

-- Seed homeplanets
INSERT INTO homeplanets (name) VALUES ('Earth') ON CONFLICT (name) DO NOTHING;
INSERT INTO homeplanets (name) VALUES ('Nibiru') ON CONFLICT (name) DO NOTHING;
INSERT INTO homeplanets (name) VALUES ('Zog Prime') ON CONFLICT (name) DO NOTHING;

-- Seed looking_for
INSERT INTO looking_for (name) VALUES ('Friendship') ON CONFLICT (name) DO NOTHING;
INSERT INTO looking_for (name) VALUES ('Romance') ON CONFLICT (name) DO NOTHING;
INSERT INTO looking_for (name) VALUES ('Alliance') ON CONFLICT (name) DO NOTHING;

-- Seed interests
INSERT INTO interests (name) VALUES ('Music') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Art') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Sports') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Travel') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Reading') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Gaming') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Cooking') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Photography') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Dancing') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Writing') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Yoga') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Hiking') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Meditation') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Movies') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Technology') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Science') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Politics') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Animals') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Fashion') ON CONFLICT (name) DO NOTHING;
INSERT INTO interests (name) VALUES ('Gardening') ON CONFLICT (name) DO NOTHING;