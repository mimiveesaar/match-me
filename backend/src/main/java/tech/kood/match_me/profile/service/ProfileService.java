package tech.kood.match_me.profile.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.repository.BodyformRepository;
import tech.kood.match_me.profile.repository.HomeplanetRepository;
import tech.kood.match_me.profile.repository.InterestRepository;
import tech.kood.match_me.profile.repository.LookingForRepository;
import tech.kood.match_me.profile.repository.ProfileRepository;

@Service
public class ProfileService {

        private final ProfileRepository profileRepo;
        private final HomeplanetRepository homeplanetRepo;
        private final BodyformRepository bodyformRepo;
        private final LookingForRepository lookingForRepo;
        private final InterestRepository interestRepo;

        public ProfileService(ProfileRepository profileRepo, HomeplanetRepository homeplanetRepo,
                        BodyformRepository bodyformRepo, LookingForRepository lookingForRepo,
                        InterestRepository interestRepo) {
                this.profileRepo = profileRepo;
                this.homeplanetRepo = homeplanetRepo;
                this.bodyformRepo = bodyformRepo;
                this.lookingForRepo = lookingForRepo;
                this.interestRepo = interestRepo;
        }

        @Transactional
        public Profile saveOrUpdateProfile(ProfileDTO dto) {
                Profile profile = profileRepo.findAll().stream().findFirst()
                                .orElseGet(() -> new Profile());


                profile.setUsername(dto.getUsername());
                profile.setAge(dto.getAge());
                profile.setBio(dto.getBio());
                profile.setProfilePic(dto.getProfilePic());

                profile.setHomeplanet(homeplanetRepo.findById(dto.getHomeplanetId())
                                .orElseThrow(() -> new RuntimeException("Homeplanet with ID "
                                                + dto.getHomeplanetId() + " not found")));
                profile.setBodyform(bodyformRepo.findById(dto.getBodyformId())
                                .orElseThrow(() -> new RuntimeException("Bodyform with ID "
                                                + dto.getBodyformId() + " not found")));
                profile.setLookingFor(lookingForRepo.findById(dto.getLookingForId())
                                .orElseThrow(() -> new RuntimeException("LookingFor with ID "
                                                + dto.getLookingForId() + " not found")));

                profile.setInterests(new HashSet<>(interestRepo.findAllById(dto.getInterestIds())));

                return profileRepo.save(profile);
        }

        public Profile getMyProfile() {
                return profileRepo.findAll().stream().findFirst()
                                .orElseThrow(() -> new RuntimeException("No profile found"));
        }
}
