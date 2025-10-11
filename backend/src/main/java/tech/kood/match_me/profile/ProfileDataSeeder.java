package tech.kood.match_me.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.kood.match_me.profile.model.*;
import tech.kood.match_me.profile.repository.*;

import java.util.UUID;

@Component
public class ProfileDataSeeder implements CommandLineRunner {

    @Autowired
    private BodyformRepository bodyformRepository;
    
    @Autowired
    private HomeplanetRepository homeplanetRepository;
    
    @Autowired
    private LookingForRepository lookingForRepository;
    
    @Autowired
    private InterestRepository interestRepository;
    
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== SEEDING PROFILE DATA ===");
        
        try {
            seedBodyforms();
            seedHomeplanets();
            seedLookingFor();
            seedInterests();
            seedTestProfiles();
            
            verifyData();
            
            System.out.println("=== PROFILE DATA SEEDING COMPLETE ===\n");
        } catch (Exception e) {
            System.err.println("\n❌ SEEDING FAILED WITH ERROR:");
            e.printStackTrace();
            System.err.println("\n");
        }
    }

    private void seedBodyforms() {
        System.out.println("🌱 Seeding bodyforms...");
        String[] bodyforms = {"Gelatinous", "Tentacled", "Humanoid", "Energy-Based", "Mechanical",
                "Insectoid", "Reptilian", "Gas Cloud", "Crystalline", "Mimetic Blob"};
        
        int inserted = 0;
        int skipped = 0;
        
        for (String name : bodyforms) {
            try {
                if (!bodyformRepository.existsByName(name)) {
                    Bodyform bodyform = new Bodyform();
                    bodyform.setName(name);
                    bodyformRepository.save(bodyform);
                    inserted++;
                    System.out.println("  ✅ Inserted: " + name);
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + name + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Bodyforms: " + inserted + " inserted, " + skipped + " skipped\n");
    }

    private void seedHomeplanets() {
        System.out.println("🌍 Seeding homeplanets...");
        String[] homeplanets = {"Xeron-5", "Draknor", "Vega Prime", "Bloop-X12", "Zal'Tek Major",
                "Nimbus-9", "Krylon Beta", "Nova Eden", "Tharnis", "Quarnyx Delta", "Glooporia",
                "Skarn", "Uvuul-4", "Oortania", "Vrexalon"};
        
        int inserted = 0;
        int skipped = 0;
        
        for (String name : homeplanets) {
            try {
                if (!homeplanetRepository.existsByName(name)) {
                    Homeplanet homeplanet = new Homeplanet();
                    homeplanet.setName(name);
                    homeplanetRepository.save(homeplanet);
                    inserted++;
                    System.out.println("  ✅ Inserted: " + name);
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + name + ": " + e.getMessage());
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
        
        for (String name : lookingFor) {
            try {
                if (!lookingForRepository.existsByName(name)) {
                    LookingFor lf = new LookingFor();
                    lf.setName(name);
                    lookingForRepository.save(lf);
                    inserted++;
                    System.out.println("  ✅ Inserted: " + name);
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + name + ": " + e.getMessage());
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
        
        for (String name : interests) {
            try {
                if (!interestRepository.existsByName(name)) {
                    Interest interest = new Interest();
                    interest.setName(name);
                    interestRepository.save(interest);
                    inserted++;
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("  ❌ Failed to insert " + name + ": " + e.getMessage());
            }
        }
        System.out.println("📊 Interests: " + inserted + " inserted, " + skipped + " skipped\n");
    }

    private void seedTestProfiles() {
        System.out.println("👤 Seeding test profiles...");
        int inserted = 0;
        int failed = 0;
        
        // Get first entities to use as references
        Homeplanet homeplanet = homeplanetRepository.findAll().stream().findFirst().orElse(null);
        Bodyform bodyform = bodyformRepository.findAll().stream().findFirst().orElse(null);
        LookingFor lookingFor = lookingForRepository.findAll().stream().findFirst().orElse(null);
        
        if (homeplanet == null || bodyform == null || lookingFor == null) {
            System.err.println("❌ Cannot seed profiles: missing reference data");
            System.err.println("   Make sure bodyforms, homeplanets, and looking_for are seeded first");
            return;
        }
        
        for (int i = 1; i <= 5; i++) {
            String username = "testuser" + i;
            
            try {
                if (!profileRepository.existsByUsername(username)) {
                    Profile profile = new Profile();
                    profile.setUserId(UUID.randomUUID());
                    profile.setUsername(username);
                    profile.setAge(20 + i);
                    profile.setBio("This is a test bio for " + username);
                    profile.setProfilePic("https://example.com/" + username + ".jpg");
                    profile.setHomeplanet(homeplanet);
                    profile.setBodyform(bodyform);
                    profile.setLookingFor(lookingFor);
                    
                    profileRepository.save(profile);
                    inserted++;
                    System.out.println("  ✅ Inserted profile: " + username);
                } else {
                    System.out.println("  ⏭️  Profile already exists: " + username);
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
        
        try {
            long bodyformCount = bodyformRepository.count();
            long homeplanetCount = homeplanetRepository.count();
            long lookingForCount = lookingForRepository.count();
            long interestCount = interestRepository.count();
            long profileCount = profileRepository.count();
            
            System.out.println("📊 Table bodyforms has " + bodyformCount + " rows");
            System.out.println("📊 Table homeplanets has " + homeplanetCount + " rows");
            System.out.println("📊 Table looking_for has " + lookingForCount + " rows");
            System.out.println("📊 Table interests has " + interestCount + " rows");
            System.out.println("📊 Table profiles has " + profileCount + " rows");
            
        } catch (Exception e) {
            System.err.println("⚠️ Cannot verify data: " + e.getMessage());
        }
        
        System.out.println("--- Verification Complete ---\n");
    }
}