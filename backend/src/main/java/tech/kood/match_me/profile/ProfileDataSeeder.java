package tech.kood.match_me.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfileDataSeeder implements CommandLineRunner {

    @Autowired
    @Qualifier("profileManagementJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== SEEDING PROFILE DATA ===");

        // First, fix the table structure and sequences
        createSequencesAndFixTables();

        // Then seed data
        seedAllData();

        System.out.println("=== PROFILE DATA SEEDING COMPLETE ===");
    }

    private void createSequencesAndFixTables() {
        String[] tables = {"bodyforms", "homeplanets", "looking_for", "interests"};

        for (String table : tables) {
            try {
                // Create sequence if it doesn't exist
                jdbcTemplate
                        .execute(String.format("CREATE SEQUENCE IF NOT EXISTS %s_id_seq", table));

                // Set the column default to use the sequence
                jdbcTemplate.execute(String.format(
                        "ALTER TABLE %s ALTER COLUMN id SET DEFAULT nextval('%s_id_seq')", table,
                        table));

                // Make the sequence owned by the table
                jdbcTemplate.execute(
                        String.format("ALTER SEQUENCE %s_id_seq OWNED BY %s.id", table, table));

                System.out.println("Fixed sequence for " + table);

            } catch (Exception e) {
                System.out.println("Error fixing sequence for " + table + ": " + e.getMessage());
            }
        }
    }

    private void seedAllData() {
        seedBodyforms();
        seedHomeplanets();
        seedLookingFor();
        seedInterests();
        verifyData();
    }

    private void seedBodyforms() {
        // Updated to match MatchingDataSeeder options
        String[] bodyforms = {"Gelatinous", "Tentacled", "Humanoid", "Energy-Based", "Mechanical",
                "Insectoid", "Reptilian", "Gas Cloud", "Crystalline", "Mimetic Blob"};
        for (String bodyform : bodyforms) {
            jdbcTemplate.update(
                    "INSERT INTO bodyforms (name) VALUES (?) ON CONFLICT (name) DO NOTHING",
                    bodyform);
        }
        System.out.println("Bodyforms seeded");
    }

    private void seedHomeplanets() {
        // Updated to match MatchingDataSeeder options
        String[] homeplanets = {"Xeron-5", "Draknor", "Vega Prime", "Bloop-X12", "Zal'Tek Major",
                "Nimbus-9", "Krylon Beta", "Nova Eden", "Tharnis", "Quarnyx Delta", "Glooporia",
                "Skarn", "Uvuul-4", "Oortania", "Vrexalon"};
        for (String homeplanet : homeplanets) {
            jdbcTemplate.update(
                    "INSERT INTO homeplanets (name) VALUES (?) ON CONFLICT (name) DO NOTHING",
                    homeplanet);
        }
        System.out.println("Homeplanets seeded");
    }

    private void seedLookingFor() {
        // Updated to match MatchingDataSeeder options
        String[] lookingFor = {"Friendship", "Romance", "Strategic Alliance",
                "Co-parenting Hatchlings", "Host Symbiosis", "Chtulhu"};
        for (String item : lookingFor) {
            jdbcTemplate.update(
                    "INSERT INTO looking_for (name) VALUES (?) ON CONFLICT (name) DO NOTHING",
                    item);
        }
        System.out.println("Looking_for seeded");
    }

    private void seedInterests() {
        // Updated to match MatchingDataSeeder expanded options
        String[] interests = {"Telepathic Chess", "Black Hole Karaoke", "Baking", "Binary Poetry",
                "Painting", "Parallel Parking", "Reading", "Collecting Rocks", "Butterfly watching",
                "Plasma Sculpting", "Terraforming", "Zero-G Yoga", "Fishing", "Galactic Geocaching",
                "Nebula Photography", "Starship Racing", "Archaeology", "Cooking",
                "Light-speed Surfing", "Wormhole Navigation", "Cryo-sleep", "Martian Mining",
                "Solar Wind Sailing", "Meditation", "Opera Singing", "Ballet", "Fashion Design",
                "Black Market Trading", "Cosmic Comics", "Meteorite Hunting",
                "Exoplanet Exploration", "Star Map Reading", "Galactic Diplomacy", "Gardening",
                "Interstellar DJing", "Teleportation Tricks", "Brewing", "Droid Repair",
                "Cryptography", "Wormhole Jumping"};

        for (String interest : interests) {
            jdbcTemplate.update(
                    "INSERT INTO interests (name) VALUES (?) ON CONFLICT (name) DO NOTHING",
                    interest);
        }
        System.out.println("Interests seeded");
    }

    private void verifyData() {
        String[] tables = {"bodyforms", "homeplanets", "looking_for", "interests"};
        for (String table : tables) {
            Integer count =
                    jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
            System.out.println("Table " + table + " has " + count + " rows");
        }
    }

    //hardcoded user for testing purposes

    private void seedDefaultUserAndProfile() {
        // Insert user
        jdbcTemplate.update(
                "INSERT INTO users (username, password) VALUES (?, ?) ON CONFLICT (username) DO NOTHING",
                "testuser", "{noop}password" // {noop} means plain-text in Spring Security
        );

        // Fetch user_id
        Long userId = jdbcTemplate.queryForObject("SELECT id FROM users WHERE username = ?",
                Long.class, "testuser");

        // Insert profile (only if it doesn’t exist yet)
        jdbcTemplate.update(
                "INSERT INTO profiles (user_id, bio, homeplanet_id, bodyform_id, looking_for_id, profile_pic) "
                        + "VALUES (?, ?, NULL, NULL, NULL, NULL) "
                        + "ON CONFLICT (user_id) DO NOTHING",
                userId, "This is a seeded bio");

        System.out.println("Default user + profile seeded (username: testuser)");
    }
}

