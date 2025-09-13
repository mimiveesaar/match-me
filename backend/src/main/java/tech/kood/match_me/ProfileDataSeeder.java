package tech.kood.match_me;

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
        seedBodyforms();
        seedHomeplanets();
        seedLookingFor();
        seedInterests();
        verifyData();
    }
    
    private void seedBodyforms() {
        String[] bodyforms = {"Humanoid", "Reptilian", "Energy Being"};
        for (String bodyform : bodyforms) {
            jdbcTemplate.update(
                "INSERT INTO bodyforms (name) VALUES (?) ON CONFLICT (name) DO NOTHING", 
                bodyform
            );
        }
        System.out.println("Bodyforms seeded");
    }
    
    private void seedHomeplanets() {
        String[] homeplanets = {"Earth", "Nibiru", "Zog Prime"};
        for (String homeplanet : homeplanets) {
            jdbcTemplate.update(
                "INSERT INTO homeplanets (name) VALUES (?) ON CONFLICT (name) DO NOTHING", 
                homeplanet
            );
        }
        System.out.println("Homeplanets seeded");
    }
    
    private void seedLookingFor() {
        String[] lookingFor = {"Friendship", "Romance", "Alliance"};
        for (String item : lookingFor) {
            jdbcTemplate.update(
                "INSERT INTO looking_for (name) VALUES (?) ON CONFLICT (name) DO NOTHING", 
                item
            );
        }
        System.out.println("Looking_for seeded");
    }
    
    private void seedInterests() {
        String[] interests = {
            "Music", "Art", "Sports", "Travel", "Reading", "Gaming", 
            "Cooking", "Photography", "Dancing", "Writing", "Yoga", 
            "Hiking", "Meditation", "Movies", "Technology", "Science", 
            "Politics", "Animals", "Fashion", "Gardening"
        };
        
        for (String interest : interests) {
            jdbcTemplate.update(
                "INSERT INTO interests (name) VALUES (?) ON CONFLICT (name) DO NOTHING", 
                interest
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