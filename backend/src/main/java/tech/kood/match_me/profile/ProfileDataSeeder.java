package tech.kood.match_me.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.constants.Bodyforms;
import tech.kood.match_me.common.constants.Interests;
import tech.kood.match_me.common.constants.LookingFor;
import tech.kood.match_me.common.constants.Planets;
import tech.kood.match_me.profile.model.*;
import tech.kood.match_me.profile.repository.*;

import java.util.stream.Collectors;

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
        for (Bodyforms bfEnum : Bodyforms.values()) {
            if (!bodyformRepository.existsByName(bfEnum.DisplayName())) {
                Bodyform bf = new Bodyform();
                bf.setId(bfEnum.Code()); // Integer ID from enum
                bf.setName(bfEnum.DisplayName());
                bodyformRepository.save(bf);
                System.out.println("  ✅ Inserted: " + bfEnum.DisplayName());
            }
        }
        System.out.println();
    }

    private void seedHomeplanets() {
        System.out.println("🌍 Seeding homeplanets...");
        for (Planets pEnum : Planets.values()) {
            if (!homeplanetRepository.existsByName(pEnum.DisplayName())) {
                Homeplanet planet = new Homeplanet();
                planet.setId(pEnum.Code()); // Integer ID from enum
                planet.setName(pEnum.DisplayName());
                homeplanetRepository.save(planet);
                System.out.println("  ✅ Inserted: " + pEnum.DisplayName());
            }
        }
        System.out.println();
    }

    private void seedLookingFor() {
        System.out.println("💕 Seeding looking_for...");
        for (LookingFor lfEnum : LookingFor.values()) {
            if (!lookingForRepository.existsByName(lfEnum.DisplayName())) {
                tech.kood.match_me.profile.model.LookingFor lf = new tech.kood.match_me.profile.model.LookingFor();
                lf.setId(lfEnum.Code()); // Integer ID from enum
                lf.setName(lfEnum.DisplayName());
                lookingForRepository.save(lf);
                System.out.println("  ✅ Inserted: " + lfEnum.DisplayName());
            }
        }
        System.out.println();
    }

    private void seedInterests() {
        System.out.println("🎯 Seeding interests...");
        for (Interests iEnum : Interests.values()) {
            if (!interestRepository.existsByName(iEnum.DisplayName())) {
                Interest interest = new Interest();
                interest.setId(iEnum.Code()); // Integer ID from enum
                interest.setName(iEnum.DisplayName());
                interestRepository.save(interest);
            }
        }
        System.out.println();
    }

    private void seedTestProfiles() {
        System.out.println("👤 Seeding test profiles...");

        Homeplanet homeplanet = homeplanetRepository.findAll().stream().findFirst().orElse(null);
        Bodyform bodyform = bodyformRepository.findAll().stream().findFirst().orElse(null);
        tech.kood.match_me.profile.model.LookingFor lookingFor = lookingForRepository.findAll().stream().findFirst().orElse(null);

        if (homeplanet == null || bodyform == null || lookingFor == null) {
            System.err.println("❌ Cannot seed profiles: missing reference data");
            return;
        }

        for (int i = 1; i <= 5; i++) {
            String username = "testuser" + i;

            if (!profileRepository.existsByUsername(username)) {
                Profile profile = new Profile();
                profile.setUserId(java.util.UUID.randomUUID());
                profile.setUsername(username);
                profile.setAge(20 + i);
                profile.setBio("This is a test bio for " + username);
                profile.setProfilePic("https://example.com/" + username + ".jpg");
                profile.setHomeplanet(homeplanet);
                profile.setBodyform(bodyform);
                profile.setLookingFor(lookingFor);
                profile.setInterests(interestRepository.findAll().stream().limit(3).collect(Collectors.toSet()));

                profileRepository.save(profile);
                System.out.println("  ✅ Inserted profile: " + username);
            }
        }
        System.out.println();
    }

    private void verifyData() {
        System.out.println("--- Final Data Verification ---");
        System.out.println("📊 Table bodyforms has " + bodyformRepository.count() + " rows");
        System.out.println("📊 Table homeplanets has " + homeplanetRepository.count() + " rows");
        System.out.println("📊 Table looking_for has " + lookingForRepository.count() + " rows");
        System.out.println("📊 Table interests has " + interestRepository.count() + " rows");
        System.out.println("📊 Table profiles has " + profileRepository.count() + " rows");
        System.out.println("--- Verification Complete ---\n");
    }
}
