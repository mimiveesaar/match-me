package tech.kood.match_me.matching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
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
public class MatchingDataSeeder implements CommandLineRunner {

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


    private void seedAllData() {
        seedBodyforms();
        seedHomeplanets();
        seedLookingFor();
        seedInterests();
    }


//
//    private void seedUserInterests() {
//        Object[][] userInterests = {
//            {"3fa85f64-5717-4562-b3fc-2c963f66afa1", 1},
//            {"3fa85f64-5717-4562-b3fc-2c963f66afa1", 5},
//            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", 3},
//            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", 6},
//            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", 10},
//            {"f2d45e1c-4d9c-4a5a-b2fa-1f55e720347a", 2},
//            {"23dc80aa-01ae-4cf3-93c0-60b3846ef1e5", 4},
//            {"23dc80aa-01ae-4cf3-93c0-60b3846ef1e5", 8},
//            {"d87e7304-7bfb-4bfb-9318-52c58f3c1036", 9},
//            {"d87e7304-7bfb-4bfb-9318-52c58f3c1036", 12},
//            {"aaab9e4b-7a3a-45cf-9408-c89e0d032871", 7},
//            {"1db4562e-93a3-4cc5-b2ff-b3cf95fd3a45", 3},
//            {"1db4562e-93a3-4cc5-b2ff-b3cf95fd3a45", 11},
//            {"c6ee9999-8371-46cf-9302-45f1ddee6a6d", 6},
//            {"c6ee9999-8371-46cf-9302-45f1ddee6a6d", 13},
//            {"4b07cd42-d78f-4fa5-bf91-f3ad3cf8db99", 1},
//            {"4b07cd42-d78f-4fa5-bf91-f3ad3cf8db99", 14},
//            {"e85e2b12-01dc-4564-b78c-d6d4c77002b9", 2},
//            {"e85e2b12-01dc-4564-b78c-d6d4c77002b9", 5},
//            {"41b5a984-9822-4bc2-9c3c-93b1d4238e6f", 4},
//            {"41b5a984-9822-4bc2-9c3c-93b1d4238e6f", 10},
//            {"dcf48c31-d8e1-4e90-8599-1e4f4c5c0977", 9},
//            {"8fc909b7-cbc1-41ce-bc70-4b41ea4a2326", 8},
//            {"8fc909b7-cbc1-41ce-bc70-4b41ea4a2326", 11},
//            {"51d00c53-7714-41cb-8cf1-8e620155f3ec", 7},
//            {"51d00c53-7714-41cb-8cf1-8e620155f3ec", 13},
//            {"19f54fd2-84ec-4213-a4ab-d81cf865fbc7", 3},
//            {"3d47007e-96e0-4d37-8663-02a6e1e23d84", 6},
//            {"3d47007e-96e0-4d37-8663-02a6e1e23d84", 14},
//            {"7c9e4e62-7e32-4ce3-872b-78370839d0f2", 1},
//            {"7c9e4e62-7e32-4ce3-872b-78370839d0f2", 4},
//            {"e4e7a4e1-4ab0-406c-b6c4-7c6cbd28f671", 10},
//            {"e4e7a4e1-4ab0-406c-b6c4-7c6cbd28f671", 11},
//            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", 2},
//            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", 5},
//            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", 8},
//            {"0b2e39ea-d1ef-4b6c-93b6-1d0ff861197e", 12},
//
//                {"e2d1c9f6-758f-4e5f-b2a4-249d7b09aa62", 23},
//                {"6b9f12d4-9245-4634-a8b7-3aa0c1eae7fc", 7},
//                {"ae2a1129-0d15-4fa4-bdb4-fd4893a6d9b9", 34},
//                {"b0d105c1-48ef-4bba-8b52-d4d4e338e25f", 12},
//                {"43f82453-8aaf-4b3a-83f9-8e56c5ac1779", 29},
//                {"13fd2ec7-e1f1-462e-bae7-404ae8a5151f", 5},
//                {"d5d92180-5041-48aa-bbc7-d5d2274cb192", 18},
//                {"e7f153bd-fb89-4959-8d5b-798b7d7298e5", 40},
//                {"f3b6e36f-e4e2-4983-a2c5-d51cbf6f2a94", 15},
//                {"a4b63de0-42cb-44ae-9251-7982355a1574", 31},
//                {"b5fe450c-1db5-4643-95ea-8bb2dd2822a1", 9},
//                {"da8c5916-dc38-40f0-a5bb-6590aa06650c", 26},
//                {"f0e27dd2-7fc6-4e56-a4fa-3bc00f1ad70e", 3},
//                {"cf1eeb2a-e816-4982-a34f-2b29c7721d96", 37},
//                {"7ce6d285-1b27-466e-a06f-31596efeb317", 14},
//                {"379e84b3-df3d-4fae-832e-98f92e9732a5", 22},
//                {"be0bd7df-8488-4c6f-b3cf-379c78f9a52b", 6},
//                {"c83b4dc9-2e49-4b9a-800f-929b5f2187b0", 33},
//                {"44fd1dc6-fcf3-4e49-87e9-d9c8aa1be265", 19},
//                {"c66b0f7b-7484-4f2e-83cb-254019f2fe9e", 28},
//                {"37c5b0f4-95b8-4d44-9e9a-f6d36fd3a6ef", 11},
//                {"ea65a3c0-3bb1-45e5-99c3-5c79bdc07d2e", 38},
//                {"d1f79fd5-5c6e-4375-86c7-135d5d718aef", 2},
//                {"a85b437c-c3e6-40cd-a8f4-c3c9fbe1deed", 25},
//                {"b103adf7-e0e0-44a2-95a0-1a34cb697cf2", 16},
//                {"5ebc1b6e-75f2-4ff5-a89f-fc672e34c7d4", 30},
//                {"109d1a74-87fd-4622-b4a6-275b2d69e05e", 8},
//                {"04c63be3-e571-4b7f-82a7-fdb9b80b243c", 35},
//                {"8a16cb9c-e1ee-4b4a-8cf6-2f792a77d646", 20},
//                {"c3f1e5e3-b093-4d47-8677-6b35f04b5cd2", 13},
//
//                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", 17},
//                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", 16},
//                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", 31},
//                {"c9be9053-3516-49f8-ae4f-63ad1c6ea755", 24},
//                {"fa04f79f-6c26-45cc-961b-79c062c3b8e0", 4},
//                {"fa04f79f-6c26-45cc-961b-79c062c3b8e0", 10},
//                {"0e76a0c6-15d4-4df2-bde0-dca35a04a909", 36},
//                {"0e76a0c6-15d4-4df2-bde0-dca35a04a909", 32},
//                {"7867ad2f-4d62-4e7f-b99d-efb7b49a08b5", 10},
//                {"7867ad2f-4d62-4e7f-b99d-efb7b49a08b5", 9},
//                {"7b78cd1c-4bd1-44e0-8134-86a10f42103a", 27},
//                {"a20896fc-0e8f-496c-926e-ec92d3429c47", 1},
//                {"a20896fc-0e8f-496c-926e-ec92d3429c47", 40},
//                {"c6f5d2a3-4e92-4493-a4f5-cb5cc070c75e", 39},
//                {"5c52b396-81d4-4137-a10b-4a3520c17282", 21},
//                {"e1d1c14c-cb9e-46c7-9e23-35e11802df56", 8},
//                {"8f94a0b9-2bd9-4c82-8d3b-589f55b0c06f", 32},
//                {"bc4e3c58-2384-4492-bbb6-ccfc7c93985e", 15},
//                {"f7b1e05e-3963-40a4-a47f-fb82e9d9e71d", 29},
//                {"0f147ecf-5e3d-413b-a512-d46e44ed0b02", 6},
//                {"f1e91259-96be-4aa5-92e2-6cc3abddfd9e", 18},
//                {"f1e91259-96be-4aa5-92e2-6cc3abddfd9e", 15},
//                {"4d7b300f-7bb3-4f25-9a49-dfaab5094e6e", 35},
//                {"9d218ed0-70f7-4db8-973e-fb04298d8edc", 12},
//                {"19d9e92a-24fc-4bd5-9f02-882f3f4a5de9", 40},
//                {"19d9e92a-24fc-4bd5-9f02-882f3f4a5de9", 8},
//                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", 3},
//                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", 17},
//                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", 9},
//                {"2b2a4cf1-7c0c-43e6-ae6b-7292d0b6a219", 26},
//                {"2b2a4cf1-7c0c-43e6-ae6b-7292d0b6a219", 27},
//                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 9},
//                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 10},
//                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 14},
//                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 19},
//                {"d9e0b40f-1654-4ec0-a12a-3069b2b3bafd", 22},
//                {"cdb5f6d5-6026-4725-baf3-18903b250dcb", 14},
//                {"10a0b3b2-28e4-45b6-8887-44b75c20a391", 31},
//                {"f4cf46cb-84db-4b1e-97ad-94844b6c6c6a", 7},
//                {"f4cf46cb-84db-4b1e-97ad-94844b6c6c6a", 8},
//                {"54dd44f7-d65a-4328-ae36-86a04bb366f0", 38},
//                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 19},
//                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 19},
//                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 12},
//                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 15},
//                {"ca8a64b9-d615-423e-b99a-d1a0dfd30217", 25},
//                {"bdd5c771-e029-47d2-a058-0fc2f62cc3ce", 11},
//                {"bdd5c771-e029-47d2-a058-0fc2f62cc3ce", 10},
//                {"c0dfe381-e7f7-4d60-b771-d6c680e6b3ae", 33},
//                {"c0dfe381-e7f7-4d60-b771-d6c680e6b3ae", 37},
//
//                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 16},
//                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 15},
//                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 12},
//                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 11},
//                {"a7ed03cf-9ec5-4f0a-a5b6-ec51d0381b2a", 28},
//                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", 5},
//                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", 34},
//                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", 32},
//                {"b0b1ac73-f21e-4684-9f3a-8f2aa6a2e5b6", 34},
//                {"dfe81c49-60dc-4e1a-8208-26779f4f26a7", 13},
//                {"d620ad8e-d7c4-4bcb-8514-29b27e9b2f62", 37},
//                {"1e985019-4b55-4d8b-91a5-11d460d41610", 2},
//                {"a741dd32-5f2e-4fa2-b16a-67b1522596e1", 23},
//                {"49f9cfc9-34e2-4d47-913c-1d9a0ecaf3ee", 30},
//                {"3a037c99-69be-4537-9a4b-81d55c82b816", 9},
//                {"8d0cb94c-f9d3-46ed-bf2c-3d8a7fd13a4c", 20},
//                {"372edabe-1a3d-4b99-92ff-58d4be2b2b2a", 40},
//                {"b0b30e5a-4d2c-4579-a5a4-7500a3913483", 11},
//                {"f1c83b3a-47e3-4558-8b3d-8c84b5608026", 27},
//                {"e91b2c1e-b982-4e50-9c35-7c79c682df64", 4},
//                {"82a4d4bc-cd96-4f90-bc6e-f33e8a274e45", 36},
//                {"cdd6d718-1edc-4a55-985b-e81a51fba650", 18},
//                {"cb3f4b0f-f9e7-4fc5-a0b6-f546ed4e6bb3", 25},
//                {"ac4170b7-55cf-446e-861b-17f45213e839", 7},
//                {"7bfeeb47-4c4b-4db0-8d2d-1b4c33238f18", 32},
//
//
//        };


    //}

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

    @Override
    public void run(String... args) throws Exception {
        seedAllData();
    }
};



