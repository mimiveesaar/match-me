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

        seedBodyforms();
        seedHomeplanets();
        seedLookingFor();
        seedInterests();
        seedTestProfiles();
        verifyData();

        System.out.println("=== PROFILE DATA SEEDING COMPLETE ===");
    }

    private void seedBodyforms() {
        String[] bodyforms = {"Gelatinous", "Tentacled", "Humanoid", "Energy-Based", "Mechanical",
                "Insectoid", "Reptilian", "Gas Cloud", "Crystalline", "Mimetic Blob"};
        for (String b : bodyforms) {
            jdbcTemplate.update(
                    "INSERT INTO bodyforms (name) VALUES (?) ON CONFLICT (name) DO NOTHING", b);
        }
    }

    private void seedHomeplanets() {
        String[] homeplanets = {"Xeron-5", "Draknor", "Vega Prime", "Bloop-X12", "Zal'Tek Major",
                "Nimbus-9", "Krylon Beta", "Nova Eden", "Tharnis", "Quarnyx Delta", "Glooporia",
                "Skarn", "Uvuul-4", "Oortania", "Vrexalon"};
        for (String h : homeplanets) {
            jdbcTemplate.update(
                    "INSERT INTO homeplanets (name) VALUES (?) ON CONFLICT (name) DO NOTHING", h);
        }
    }

    private void seedLookingFor() {
        String[] lookingFor = {"Friendship", "Romance", "Strategic Alliance",
                "Co-parenting Hatchlings", "Host Symbiosis", "Chtulhu"};
        for (String l : lookingFor) {
            jdbcTemplate.update(
                    "INSERT INTO looking_for (name) VALUES (?) ON CONFLICT (name) DO NOTHING", l);
        }
    }

    private void seedInterests() {
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
        for (String i : interests) {
            jdbcTemplate.update(
                    "INSERT INTO interests (name) VALUES (?) ON CONFLICT (name) DO NOTHING", i);
        }
    }

    private void seedTestProfiles() {
        // Add multiple test profiles directly into profiles table
        for (int i = 1; i <= 5; i++) {
            String username = "testuser" + i;
            String bio = "This is a test bio for " + username;
            String profilePic = "https://example.com/" + username + ".jpg";

            try {
                jdbcTemplate.update(
                        "INSERT INTO profiles (username, age, bio, profile_pic, homeplanet_id, bodyform_id, looking_for_id) " +
                                "VALUES (?, ?, ?, ?, 1, 1, 1) ON CONFLICT (username) DO NOTHING",
                        username, 20 + i, bio, profilePic);
            } catch (Exception e) {
                System.out.println("Failed to insert profile " + username + ": " + e.getMessage());
            }
        }
    }

    private void verifyData() {
        String[] tables = {"bodyforms", "homeplanets", "looking_for", "interests", "profiles"};
        for (String table : tables) {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                System.out.println("Table " + table + " has " + count + " rows");
            } catch (Exception e) {
                System.out.println("Cannot count rows in " + table + ": " + e.getMessage());
            }
        }
    }
}
