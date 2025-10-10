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
        System.out.println("\n=== SEEDING PROFILE DATA ===");
        System.out.println("Seeder is running with args: " + String.join(", ", args));
        
        try {
            // Test database connection first
            testConnection();
            
            // Check if tables exist
            verifyTablesExist();
            
            // Seed data
            ensureUserIdColumnExists();
            seedBodyforms();
            seedHomeplanets();
            seedLookingFor();
            seedInterests();
            seedTestProfiles();
            
            // Verify results
            verifyData();
            
            System.out.println("=== PROFILE DATA SEEDING COMPLETE ===\n");
        } catch (Exception e) {
            System.err.println("\n❌ SEEDING FAILED WITH ERROR:");
            e.printStackTrace();
            System.err.println("\n");
        }
    }

    private void testConnection() {
        try {
            String dbName = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
            System.out.println("✅ Connected to database: " + dbName);
            
            String user = jdbcTemplate.queryForObject("SELECT current_user", String.class);
            System.out.println("✅ Connected as user: " + user);
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            throw new RuntimeException("Cannot connect to database", e);
        }
    }

    private void verifyTablesExist() {
        System.out.println("\n--- Verifying Tables ---");
        String[] tables = {"bodyforms", "homeplanets", "looking_for", "interests", "profiles"};
        boolean allExist = true;
        
        for (String table : tables) {
            try {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                System.out.println("✅ Table exists: " + table);
            } catch (Exception e) {
                System.err.println("❌ Table missing or inaccessible: " + table);
                System.err.println("   Error: " + e.getMessage());
                allExist = false;
            }
        }
        
        if (!allExist) {
            throw new RuntimeException("Some required tables are missing. Check your schema creation.");
        }
        System.out.println("--- All Required Tables Exist ---\n");
    }

    private void ensureUserIdColumnExists() {
        System.out.println("--- Ensuring user_id column ---");
        try {
            jdbcTemplate.execute(
                "ALTER TABLE profiles ADD COLUMN IF NOT EXISTS user_id uuid UNIQUE NOT NULL DEFAULT gen_random_uuid()"
            );
            System.out.println("✅ Ensured user_id column exists on profiles table\n");
        } catch (Exception e) {
            System.err.println("⚠️ Could not add user_id column: " + e.getMessage() + "\n");
        }
    }

    private void seedBodyforms() {
        System.out.println("🌱 Seeding bodyforms...");
        String[] bodyforms = {"Gelatinous", "Tentacled", "Humanoid", "Energy-Based", "Mechanical",
                "Insectoid", "Reptilian", "Gas Cloud", "Crystalline", "Mimetic Blob"};
        int inserted = 0;
        int skipped = 0;
        
        for (String b : bodyforms) {
            try {
                int rows = jdbcTemplate.update(
                    "INSERT INTO bodyforms (name) VALUES (?) ON CONFLICT (name) DO NOTHING", b
                );
                if (rows > 0) {
                    inserted++;
                    System.out.println("  ✅ Inserted: " + b);
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + b + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Bodyforms: " + inserted + " inserted, " + skipped + " skipped (already exist)\n");
    }

    private void seedHomeplanets() {
        System.out.println("🌍 Seeding homeplanets...");
        String[] homeplanets = {"Xeron-5", "Draknor", "Vega Prime", "Bloop-X12", "Zal'Tek Major",
                "Nimbus-9", "Krylon Beta", "Nova Eden", "Tharnis", "Quarnyx Delta", "Glooporia",
                "Skarn", "Uvuul-4", "Oortania", "Vrexalon"};
        int inserted = 0;
        int skipped = 0;
        
        for (String h : homeplanets) {
            try {
                int rows = jdbcTemplate.update(
                    "INSERT INTO homeplanets (name) VALUES (?) ON CONFLICT (name) DO NOTHING", h
                );
                if (rows > 0) {
                    inserted++;
                    System.out.println("  ✅ Inserted: " + h);
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + h + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Homeplanets: " + inserted + " inserted, " + skipped + " skipped\n");
    }

    private void seedLookingFor() {
        System.out.println("💕 Seeding looking_for...");
        String[] lookingFor = {"Friendship", "Romance", "Strategic Alliance",
                "Co-parenting Hatchlings", "Host Symbiosis", "Chtulhu"};
        int inserted = 0;
        int skipped = 0;
        
        for (String l : lookingFor) {
            try {
                int rows = jdbcTemplate.update(
                    "INSERT INTO looking_for (name) VALUES (?) ON CONFLICT (name) DO NOTHING", l
                );
                if (rows > 0) {
                    inserted++;
                    System.out.println("  ✅ Inserted: " + l);
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + l + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Looking_for: " + inserted + " inserted, " + skipped + " skipped\n");
    }

    private void seedInterests() {
        System.out.println("🎯 Seeding interests...");
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
        int inserted = 0;
        int skipped = 0;
        
        for (String i : interests) {
            try {
                int rows = jdbcTemplate.update(
                    "INSERT INTO interests (name) VALUES (?) ON CONFLICT (name) DO NOTHING", i
                );
                if (rows > 0) {
                    inserted++;
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + i + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Interests: " + inserted + " inserted, " + skipped + " skipped\n");
    }

    private void seedTestProfiles() {
        System.out.println("👤 Seeding test profiles...");
        int inserted = 0;
        int failed = 0;
        
        for (int i = 1; i <= 5; i++) {
            String username = "testuser" + i;
            String bio = "This is a test bio for " + username;
            String profilePic = "https://example.com/" + username + ".jpg";

            try {
                int rows = jdbcTemplate.update(
                    "INSERT INTO profiles (user_id, username, age, bio, profile_pic, homeplanet_id, bodyform_id, looking_for_id) " +
                    "VALUES (gen_random_uuid(), ?, ?, ?, ?, 1, 1, 1) ON CONFLICT (username) DO NOTHING",
                    username, 20 + i, bio, profilePic
                );
                if (rows > 0) {
                    inserted++;
                    System.out.println("  ✅ Inserted profile: " + username);
                }
            } catch (Exception e) {
                failed++;
                System.err.println("  ❌ Failed to insert profile " + username + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Profiles: " + inserted + " inserted, " + failed + " failed\n");
    }

    private void verifyData() {
        System.out.println("--- Final Data Verification ---");
        String[] tables = {"bodyforms", "homeplanets", "looking_for", "interests", "profiles"};
        
        for (String table : tables) {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                System.out.println("📊 Table " + table + " has " + count + " rows");
            } catch (Exception e) {
                System.err.println("⚠️ Cannot count rows in " + table + ": " + e.getMessage());
            }
        }
        System.out.println("--- Verification Complete ---\n");
    }
}