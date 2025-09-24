package tech.kood.match_me.matching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MatchingDataSeeder implements CommandLineRunner {

    @Autowired
    @Qualifier("matchingJdbcTemplate") //?
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== SEEDING MATCHING DATA ===");

//        // First, fix the table structure and sequences
//        createSequencesAndFixTables();
//
//        // Then seed data
//        seedAllData();

        System.out.println("=== MATCHING DATA SEEDING COMPLETE ===");
    }

    private void createSequencesAndFixTables() {
        String[] tables = {"users", "user_interests", "looking_for", "interests", "homeplanets", "bodyforms"};

        for (String table : tables) {
            try {
                // Create sequence if it doesn't exist
                jdbcTemplate.execute(String.format(
                        "CREATE SEQUENCE IF NOT EXISTS %s_id_seq", table
                ));

                // Set the column default to use the sequence
                jdbcTemplate.execute(String.format(
                        "ALTER TABLE %s ALTER COLUMN id SET DEFAULT nextval('%s_id_seq')",
                        table, table
                ));

                // Make the sequence owned by the table
                jdbcTemplate.execute(String.format(
                        "ALTER SEQUENCE %s_id_seq OWNED BY %s.id",
                        table, table
                ));

                System.out.println("Fixed sequence for " + table);

            } catch (Exception e) {
                System.out.println("Error fixing sequence for " + table + ": " + e.getMessage());
            }
        }
    }

    private void seedAllData() {
        seedUsers();
        seedUserInterests();
        seedLookingFor();
        seedInterests();
        seedHomeplanets();
        seedBodyforms();
        verifyData();
    }

    private void seedUsers() {
        Object[][] users = {
            {"3fa85f64-5717-4562-b3fc-2c963f66afa1", "starhopper42", 27, "/images/profiles/starhopper.png", 1, 1, 1, "Explorer of galaxies and maker of maps."},
            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", "nebular_nikki", 31, "/images/profiles/nebular_nikki.png", 2, 2, 2, "Dreaming beyond the stars."},
            {"f2d45e1c-4d9c-4a5a-b2fa-1f55e720347a", "cosmicjoe", 35, "/images/profiles/cosmic-joe.png", 1, 1, 1, "Looking for a co-pilot in life."},
            {"23dc80aa-01ae-4cf3-93c0-60b3846ef1e5", "gravitygal", 29, "/images/profiles/gravitygal.png", 3, 3, 3, "I never fall... unless it’s for someone."},
            {"d87e7304-7bfb-4bfb-9318-52c58f3c1036", "andromedaboy", 22, "/images/profiles/andromedaboy.png", 2, 3, 3, "Just a guy with a telescope and big dreams."},
            {"aaab9e4b-7a3a-45cf-9408-c89e0d032871", "orionquest", 40, "/images/profiles/orionquest.png", 4, 4, 4, "Long-time traveler, short-time sleeper."},
            {"1db4562e-93a3-4cc5-b2ff-b3cf95fd3a45", "venusian_vibe", 26, "/images/profiles/venusian_vibe.png", 5, 5, 5, "Bringing sunshine wherever I go."},
            {"c6ee9999-8371-46cf-9302-45f1ddee6a6d", "marsmaven", 33, "/images/profiles/marsmaven.png", 2, 1, 2, "Martian-born, Earth-raised, curiosity-fueled."},
            {"4b07cd42-d78f-4fa5-bf91-f3ad3cf8db99", "astrochick", 28, "/images/profiles/astrochick.png", 6, 6, 6, "Rockets, robots, and romance."},
            {"e85e2b12-01dc-4564-b78c-d6d4c77002b9", "quantumquinn", 38, "/images/profiles/quantumquinn.png", 3, 3, 2, "Entangled in all the right ways."},
            {"41b5a984-9822-4bc2-9c3c-93b1d4238e6f", "lunar_luke", 24, "/images/profiles/lunar_luke.png", 7, 1, 7, "Moon-walker in search of a dance partner."},
            {"dcf48c31-d8e1-4e90-8599-1e4f4c5c0977", "plutonianpixie", 30, "/images/profiles/plutonianpixie.png", 3, 3, 3, "Cold hands, warm heart, outer orbit dreams."},
            {"8fc909b7-cbc1-41ce-bc70-4b41ea4a2326", "galaxygazer", 36, "/images/profiles/galaxygazer.png", 8, 2, 8, "My telescope sees far, but I’m looking for someone near."},
            {"51d00c53-7714-41cb-8cf1-8e620155f3ec", "astro_ari", 27, "/images/profiles/astro-ari.png", 2, 2, 2, "Martinis and meteor showers."},
            {"19f54fd2-84ec-4213-a4ab-d81cf865fbc7", "jupiterjazz", 42, "/images/profiles/jupiterjazz.png", 9, 3, 9, "Rhythms of the cosmos, melodies of love."},
            {"3d47007e-96e0-4d37-8663-02a6e1e23d84", "zenzara", 21, "/images/profiles/zenzara.png", 10, 3, 3, "Meditating in zero gravity."},
            {"7c9e4e62-7e32-4ce3-872b-78370839d0f2", "nova_nate", 34, "/images/profiles/nova-nate.png", 11, 4, 10, "Burn bright, love brighter."},
            {"e4e7a4e1-4ab0-406c-b6c4-7c6cbd28f671", "solarys", 29, "/images/profiles/solarys.png", 12, 5, 1, "Daylight dreamer, starlight lover."},
            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", "wormholewendy", 26, "/images/profiles/wormholewendy.png", 13, 6, 2, "Faster than light, slower to trust."},
            {"0b2e39ea-d1ef-4b6c-93b6-1d0ff861197e", "kepler_kai", 37, "/images/profiles/kepler-kai.png", 14, 1, 3, "Mapping hearts like exoplanets."}
        };

        for (Object[] user : users) {
            jdbcTemplate.update(
                    "INSERT INTO users (id, username, age, profilepic_src, homeplanet_id, bodyform_id, looking_for_id, bio) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
                    + "ON CONFLICT (id) DO NOTHING",
                    user
            );
        }
        System.out.println("Users seeded");
    }

    private void seedUserInterests() {
        Object[][] userInterests = {
            {"3fa85f64-5717-4562-b3fc-2c963f66afa1", 1},
            {"3fa85f64-5717-4562-b3fc-2c963f66afa1", 5},
            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", 3},
            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", 6},
            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", 10},
            {"f2d45e1c-4d9c-4a5a-b2fa-1f55e720347a", 2},
            {"23dc80aa-01ae-4cf3-93c0-60b3846ef1e5", 4},
            {"23dc80aa-01ae-4cf3-93c0-60b3846ef1e5", 8},
            {"d87e7304-7bfb-4bfb-9318-52c58f3c1036", 9},
            {"d87e7304-7bfb-4bfb-9318-52c58f3c1036", 12},
            {"aaab9e4b-7a3a-45cf-9408-c89e0d032871", 7},
            {"1db4562e-93a3-4cc5-b2ff-b3cf95fd3a45", 3},
            {"1db4562e-93a3-4cc5-b2ff-b3cf95fd3a45", 11},
            {"c6ee9999-8371-46cf-9302-45f1ddee6a6d", 6},
            {"c6ee9999-8371-46cf-9302-45f1ddee6a6d", 13},
            {"4b07cd42-d78f-4fa5-bf91-f3ad3cf8db99", 1},
            {"4b07cd42-d78f-4fa5-bf91-f3ad3cf8db99", 14},
            {"e85e2b12-01dc-4564-b78c-d6d4c77002b9", 2},
            {"e85e2b12-01dc-4564-b78c-d6d4c77002b9", 5},
            {"41b5a984-9822-4bc2-9c3c-93b1d4238e6f", 4},
            {"41b5a984-9822-4bc2-9c3c-93b1d4238e6f", 10},
            {"dcf48c31-d8e1-4e90-8599-1e4f4c5c0977", 9},
            {"8fc909b7-cbc1-41ce-bc70-4b41ea4a2326", 8},
            {"8fc909b7-cbc1-41ce-bc70-4b41ea4a2326", 11},
            {"51d00c53-7714-41cb-8cf1-8e620155f3ec", 7},
            {"51d00c53-7714-41cb-8cf1-8e620155f3ec", 13},
            {"19f54fd2-84ec-4213-a4ab-d81cf865fbc7", 3},
            {"3d47007e-96e0-4d37-8663-02a6e1e23d84", 6},
            {"3d47007e-96e0-4d37-8663-02a6e1e23d84", 14},
            {"7c9e4e62-7e32-4ce3-872b-78370839d0f2", 1},
            {"7c9e4e62-7e32-4ce3-872b-78370839d0f2", 4},
            {"e4e7a4e1-4ab0-406c-b6c4-7c6cbd28f671", 10},
            {"e4e7a4e1-4ab0-406c-b6c4-7c6cbd28f671", 11},
            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", 2},
            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", 5},
            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", 8},
            {"0b2e39ea-d1ef-4b6c-93b6-1d0ff861197e", 12}
        };

        for (Object[] ui : userInterests) {
            jdbcTemplate.update(
                    "INSERT INTO user_interests (user_id, interest_id) "
                    + "VALUES (?, ?) "
                    + "ON CONFLICT (user_id, interest_id) DO NOTHING",
                    ui[0], ui[1]
            );
        }
        System.out.println("User interests seeded");
    }

    private void seedBodyforms() {
        // Each entry: {id, name}
        Object[][] bodyforms = {
            {1, "Gelatinous"},
            {2, "Tentacled"},
            {3, "Humanoid"},
            {4, "Energy-Based"},
            {5, "Mechanical"},
            {6, "Insectoid"},
            {7, "Reptilian"},
            {8, "Gas Cloud"},
            {9, "Crystalline"},
            {10, "Mimetic Blob"}
        };

        for (Object[] bf : bodyforms) {
            jdbcTemplate.update(
                    "INSERT INTO bodyforms (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
                    bf[0], bf[1]
            );
        }

        System.out.println("Bodyforms seeded");
    }

    private void seedHomeplanets() {
        // Each entry: {id, name, latitude, longitude}
        Object[][] homeplanets = {
            {1, "Xeron-5", 45.2, 130.5},
            {2, "Draknor", -33.1, 102.9},
            {3, "Vega Prime", 12.5, -45.0},
            {4, "Bloop-X12", 78.3, 60.2},
            {5, "Zal'Tek Major", -82.0, -135.7},
            {6, "Nimbus-9", 5.4, 80.1},
            {7, "Krylon Beta", -25.6, 145.3},
            {8, "Nova Eden", 33.3, -73.2},
            {9, "Tharnis", -47.8, 12.0},
            {10, "Quarnyx Delta", 66.0, -88.8},
            {11, "Glooporia", 15.2, 140.0},
            {12, "Skarn", -10.5, -120.6},
            {13, "Uvuul-4", 58.9, 170.0},
            {14, "Oortania", -60.4, -40.1},
            {15, "Vrexalon", 0.0, 0.0}
        };

        for (Object[] planet : homeplanets) {
            jdbcTemplate.update(
                    "INSERT INTO homeplanets (id, name, latitude, longitude) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO NOTHING",
                    planet[0], planet[1], planet[2], planet[3]
            );
        }

        System.out.println("Homeplanets seeded");
    }

    private void seedLookingFor() {
        // Each entry: {id, name}
        Object[][] lookingFor = {
            {1, "Friendship"},
            {2, "Romance"},
            {3, "Strategic Alliance"},
            {4, "Co-parenting Hatchlings"},
            {5, "Host Symbiosis"},
            {6, "Chtulhu"}
        };

        for (Object[] item : lookingFor) {
            jdbcTemplate.update(
                    "INSERT INTO looking_for (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
                    item[0], item[1]
            );
        }

        System.out.println("Looking_for seeded");
    }

    private void seedInterests() {
        // Each entry: {id, interest}
        Object[][] interests = {
            {1, "Telepathic Chess"},
            {2, "Black Hole Karaoke"},
            {3, "Baking"},
            {4, "Binary Poetry"},
            {5, "Painting"},
            {6, "Parallel Parking"},
            {7, "Reading"},
            {8, "Collecting Rocks"},
            {9, "Butterfly watching"},
            {10, "Plasma Sculpting"},
            {11, "Terraforming"},
            {12, "Zero-G Yoga"},
            {13, "Fishing"},
            {14, "Galactic Geocaching"},
            {15, "Nebula Photography"},
            {16, "Starship Racing"},
            {17, "Archaeology"},
            {18, "Cooking"},
            {19, "Light-speed Surfing"},
            {20, "Wormhole Navigation"},
            {21, "Cryo-sleep"},
            {22, "Martian Mining"},
            {23, "Solar Wind Sailing"},
            {24, "Meditation"},
            {25, "Opera Singing"},
            {26, "Ballet"},
            {27, "Fashion Design"},
            {28, "Black Market Trading"},
            {29, "Cosmic Comics"},
            {30, "Meteorite Hunting"},
            {31, "Exoplanet Exploration"},
            {32, "Star Map Reading"},
            {33, "Galactic Diplomacy"},
            {34, "Gardening"},
            {35, "Interstellar DJing"},
            {36, "Teleportation Tricks"},
            {37, "Brewing"},
            {38, "Droid Repair"},
            {39, "Cryptography"},
            {40, "Wormhole Jumping"}
        };

        for (Object[] interest : interests) {
            jdbcTemplate.update(
                    "INSERT INTO interests (id, interest) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
                    interest[0], interest[1]
            );
        }

        System.out.println("Interests seeded");
    }

    private void verifyData() {
        String[] tables = {"bodyforms", "homeplanets", "looking_for", "interests"};
        for (String table : tables) {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
            System.out.println("Table " + table + " has " + count + " rows");
        }
    }
}
