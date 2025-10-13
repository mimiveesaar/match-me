package tech.kood.match_me.matching.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.constants.Bodyforms;
import tech.kood.match_me.common.constants.Interests;
import tech.kood.match_me.common.constants.LookingFor;
import tech.kood.match_me.common.constants.Planets;
import tech.kood.match_me.matching.model.BodyformEntity;
import tech.kood.match_me.matching.model.HomeplanetEntity;
import tech.kood.match_me.matching.model.InterestEntity;
import tech.kood.match_me.matching.model.LookingForEntity;
import tech.kood.match_me.matching.repository.BodyformRepository;
import tech.kood.match_me.matching.repository.InterestsRepository;
import tech.kood.match_me.matching.repository.LookingForRepository;
import tech.kood.match_me.matching.repository.PlanetsRepository;

@Component
public class MatchesSeeder {

    @Autowired
    @Qualifier("MatchingBodyformRepository")
    private BodyformRepository bodyformRepository;

    @Autowired
    @Qualifier("MatchingInterestsRepository")
    private InterestsRepository interestRepository;

    @Autowired
    @Qualifier("MatchingLookingForRepository")
    private LookingForRepository lookingForRepository;

    @Autowired
    @Qualifier("MatchingPlanetsRepository")
    private PlanetsRepository planetsRepository;

    public void seedMatches() {
        seedBodyforms();
        seedInterests();
        seedLookingFor();
        seedHomeplanets();
    }

    private void seedBodyforms() {
        System.out.println("🌱 Seeding bodyforms...");
        for (Bodyforms bfEnum : Bodyforms.values()) {
            if (!bodyformRepository.existsById(bfEnum.Code())) {
                BodyformEntity bf = new BodyformEntity(bfEnum.Code(), bfEnum.DisplayName());

                bodyformRepository.save(bf);
                System.out.println("  ✅ Inserted: " + bfEnum.DisplayName());
            }
        }
    }

    private void seedHomeplanets() {
        System.out.println("🌍 Seeding homeplanets...");
        for (Planets pEnum : Planets.values()) {
            if (!planetsRepository.existsById(pEnum.Code())) {
                HomeplanetEntity planet = new HomeplanetEntity(pEnum.Code(), pEnum.DisplayName(), pEnum.Latitude(), pEnum.Longitude());

                planetsRepository.save(planet);
                System.out.println("  ✅ Inserted: " + pEnum.DisplayName());
            }
        }
    }

    private void seedLookingFor() {
        System.out.println("💕 Seeding looking_for...");
        for (LookingFor lfEnum : LookingFor.values()) {
            if (!lookingForRepository.existsById(lfEnum.Code())) {
                LookingForEntity lf = new LookingForEntity(lfEnum.Code(), lfEnum.DisplayName());

                lookingForRepository.save(lf);
                System.out.println("  ✅ Inserted: " + lfEnum.DisplayName());
            }
        }
    }

    private void seedInterests() {
        System.out.println("🎯 Seeding interests...");
        for (Interests iEnum : Interests.values()) {
            if (!interestRepository.existsById(iEnum.Code())) {
                InterestEntity interest = new InterestEntity(iEnum.Code(), iEnum.DisplayName());

                interestRepository.save(interest);
            }
        }
    }
}
