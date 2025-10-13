package tech.kood.match_me.matching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.constants.Bodyforms;
import tech.kood.match_me.common.constants.Interests;
import tech.kood.match_me.common.constants.LookingFor;
import tech.kood.match_me.common.constants.Planets;

import java.util.Map;
import java.util.UUID;

@Component
public class MatchingDataSeeder implements CommandLineRunner {

    @Autowired
    @Qualifier("matchingJdbcTemplate") //?
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {

        return;

//        System.out.println("=== SEEDING MATCHING DATA ===");
//
//        // First, fix the table structure and sequences
//        createSequencesAndFixTables();
//
//        // Then seed data
//        seedAllData();
//
//        System.out.println("=== MATCHING DATA SEEDING COMPLETE ===");
    }

    private void createSequencesAndFixTables() {
        Map<String, String> columnTypes = Map.of(
                "users", "uuid",
                "user_interests", "uuid", // guessing here – verify
                "interests", "bigint",
                "looking_for", "integer",
                "homeplanets", "integer",
                "bodyforms", "integer"
        );

        for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
            String table = entry.getKey();
            String type = entry.getValue();

            if (!type.equals("integer")) {
                System.out.println("Skipping sequence fix for non-integer ID column: " + table + " (" + type + ")");
                continue;
            }

            try {
                jdbcTemplate.execute(String.format(
                        "CREATE SEQUENCE IF NOT EXISTS %s_id_seq", table
                ));

                jdbcTemplate.execute(String.format(
                        "ALTER TABLE %s ALTER COLUMN id SET DEFAULT nextval('%s_id_seq')",
                        table, table
                ));

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
        seedUsers();
        seedUserInterests();
        verifyData();
    }

    private void seedUsers() {
        Object[][] users = {
            {"3fa85f64-5717-4562-b3fc-2c963f66afa1", "starhopper42", 27, "/images/profiles/starhopper.png", 1, 1, 1, "Explorer of galaxies and maker of maps."},
            {"c9a6463d-5e4b-43ef-924e-25d7a9e8e6c2", "nebular_nikki", 31, "/images/profiles/nebular-nikki.png", 2, 2, 2, "Dreaming beyond the stars."},
            {"f2d45e1c-4d9c-4a5a-b2fa-1f55e720347a", "cosmicjoe", 35, "/images/profiles/cosmic-joe.png", 1, 1, 1, "Looking for a co-pilot in life."},
            {"23dc80aa-01ae-4cf3-93c0-60b3846ef1e5", "gravitygal", 29, "/images/profiles/gravitygal.png", 3, 3, 3, "I never fall... unless it’s for someone."},
            {"d87e7304-7bfb-4bfb-9318-52c58f3c1036", "andromedaboy", 22, "/images/profiles/andromedaboy.png", 2, 3, 3, "Just a guy with a telescope and big dreams."},
            {"aaab9e4b-7a3a-45cf-9408-c89e0d032871", "orionquest", 40, "/images/profiles/orionquest.png", 4, 4, 4, "Long-time traveler, short-time sleeper."},
            {"1db4562e-93a3-4cc5-b2ff-b3cf95fd3a45", "venusian_vibe", 26, "/images/profiles/venusian_vibe.png", 5, 5, 5, "Bringing sunshine wherever I go."},
            {"c6ee9999-8371-46cf-9302-45f1ddee6a6d", "marsmaven", 33, "/images/profiles/marsmaven.png", 2, 1, 2, "Martian-born, Earth-raised, curiosity-fueled."},
            {"4b07cd42-d78f-4fa5-bf91-f3ad3cf8db99", "astrochick", 28, "/images/profiles/astrochick.png", 6, 6, 6, "Rockets, robots, and romance."},
            {"e85e2b12-01dc-4564-b78c-d6d4c77002b9", "quantumquinn", 38, "/images/profiles/quantumquinn.png", 3, 3, 2, "Entangled in all the right ways."},
            {"41b5a984-9822-4bc2-9c3c-93b1d4238e6f", "lunar_luke", 24, "/images/profiles/lunar_luke.png", 7, 1, 3, "Moon-walker in search of a dance partner."},
            {"dcf48c31-d8e1-4e90-8599-1e4f4c5c0977", "plutonianpixie", 30, "/images/profiles/plutonianpixie.png", 3, 3, 3, "Cold hands, warm heart, outer orbit dreams."},
            {"8fc909b7-cbc1-41ce-bc70-4b41ea4a2326", "galaxygazer", 36, "/images/profiles/galaxygazer.png", 8, 2, 6, "My telescope sees far, but I’m looking for someone near."},
            {"51d00c53-7714-41cb-8cf1-8e620155f3ec", "astro_ari", 27, "/images/profiles/astro-ari.png", 2, 2, 2, "Martinis and meteor showers."},
            {"19f54fd2-84ec-4213-a4ab-d81cf865fbc7", "jupiterjazz", 42, "/images/profiles/jupiterjazz.png", 9, 3, 2, "Rhythms of the cosmos, melodies of love."},
            {"3d47007e-96e0-4d37-8663-02a6e1e23d84", "zenzara", 21, "/images/profiles/zenzara.png", 10, 3, 3, "Meditating in zero gravity."},
            {"7c9e4e62-7e32-4ce3-872b-78370839d0f2", "nova_nate", 34, "/images/profiles/nova-nate.png", 11, 4, 4, "Burn bright, love brighter."},
            {"e4e7a4e1-4ab0-406c-b6c4-7c6cbd28f671", "solarys", 29, "/images/profiles/solarys.png", 12, 5, 1, "Daylight dreamer, starlight lover."},
            {"bfd6b738-6c58-4b77-8aa2-dce297fa23b5", "wormholewendy", 26, "/images/profiles/wormholewendy.png", 13, 6, 2, "Faster than light, slower to trust."},
            {"0b2e39ea-d1ef-4b6c-93b6-1d0ff861197e", "kepler_kai", 37, "/images/profiles/kepler-kai.png", 14, 1, 3, "Mapping hearts like exoplanets."},

                {"e2d1c9f6-758f-4e5f-b2a4-249d7b09aa62", "khebab", 47, "/images/profiles/khebab.png", 1, 2, 6, "47 in human years."},
                {"6b9f12d4-9245-4634-a8b7-3aa0c1eae7fc", "goldie_brains", 121, "/images/profiles/goldie-brains.png", 2, 1, 5, "Knows 47 ways to win at chess but forgets her keys every day."},
                {"ae2a1129-0d15-4fa4-bdb4-fd4893a6d9b9", "gong", 107, "/images/profiles/gong.png", 3, 2, 4, "Speaks only in haikus and kitchen metaphors."},
                {"b0d105c1-48ef-4bba-8b52-d4d4e338e25f", "Krik", 19, "/images/profiles/krik.png", 4, 3, 3, "I have a twin brother. He’s slightly less cool."},
                {"43f82453-8aaf-4b3a-83f9-8e56c5ac1779", "Krak", 19, "/images/profiles/krak.png", 4, 3, 3, "I have a twin brother. He thinks he’s cooler."},
                {"13fd2ec7-e1f1-462e-bae7-404ae8a5151f", "siip", 78, "/images/profiles/siip.png", 6, 5, 1, "Retired pirate, current sudoku champion."},
                {"d5d92180-5041-48aa-bbc7-d5d2274cb192", "potaroo", 40, "/images/profiles/potaroo.png", 7, 2, 6, "Allergic to drama, addicted to hummus."},
                {"e7f153bd-fb89-4959-8d5b-798b7d7298e5", "loonToon", 150, "/images/profiles/loontoon.png", 8, 7, 3, "Still waiting for their cartoon royalty checks."},
                {"f3b6e36f-e4e2-4983-a2c5-d51cbf6f2a94", "Bünf", 32, "/images/profiles/bunf.png", 9, 8, 4, "Collects spoons. Not ironically."},
                {"a4b63de0-42cb-44ae-9251-7982355a1574", "Grong", 79, "/images/profiles/grong.png", 10, 9, 5, "Built like a tank, cries during baking shows."},
                {"b5fe450c-1db5-4643-95ea-8bb2dd2822a1", "LupiLuu", 80, "/images/profiles/lupiluu.png", 11, 10, 6, "Can out-sing a canary and out-nap a cat."},
                {"da8c5916-dc38-40f0-a5bb-6590aa06650c", "Krox", 59, "/images/profiles/krox.png", 12, 9, 5, "Gym rat in the streets, poet in the tweets."},
                {"f0e27dd2-7fc6-4e56-a4fa-3bc00f1ad70e", "Paxy", 67, "/images/profiles/paxy.png", 13, 8, 4, "Thinks space is cool, still afraid of elevators."},
                {"cf1eeb2a-e816-4982-a34f-2b29c7721d96", "Spongey", 68, "/images/profiles/spongey.png", 14, 7, 3, "Absorbs vibes and conspiracy theories."},
                {"7ce6d285-1b27-466e-a06f-31596efeb317", "Seesamike", 33, "/images/profiles/seesamike.png", 15, 6, 2, "Knows the secret recipe, won’t tell a soul."},
                {"379e84b3-df3d-4fae-832e-98f92e9732a5", "Zobra", 99, "/images/profiles/zobra.png", 1, 5, 1, "Once won a staring contest with a statue."},
                {"be0bd7df-8488-4c6f-b3cf-379c78f9a52b", "Cafu", 75, "/images/profiles/cafu.png", 2, 4, 2, "Retired time traveler. Not allowed near microwaves."},
                {"c83b4dc9-2e49-4b9a-800f-929b5f2187b0", "Bermylla", 88, "/images/profiles/bermylla.png", 3, 3, 3, "Got lost in a hedge maze in 1973. Still not sure if out."},
                {"44fd1dc6-fcf3-4e49-87e9-d9c8aa1be265", "Bebiku", 93, "/images/profiles/bebiku.png", 4, 2, 4, "Dances like everyone’s watching. And charges admission."},
                {"c66b0f7b-7484-4f2e-83cb-254019f2fe9e", "Sepik", 29, "/images/profiles/sepik.png", 5, 1, 5, "Once sneezed and invented a new form of jazz."},

                {"37c5b0f4-95b8-4d44-9e9a-f6d36fd3a6ef", "zorblax_77", 120, "/images/profiles/zorblax-77.png", 2, 5, 4, "Fluent in over 6,000 love dialects."},
                {"ea65a3c0-3bb1-45e5-99c3-5c79bdc07d2e", "luma_vyra", 29, "/images/profiles/luma-vyra.png", 11, 2, 1, "Seeks someone to harmonize neural waves."},
                {"d1f79fd5-5c6e-4375-86c7-135d5d718aef", "qorrin_xel", 54, "/images/profiles/qorrin-xel.png", 4, 9, 3, "Half-squid, all heart."},
                {"a85b437c-c3e6-40cd-a8f4-c3c9fbe1deed", "nebulynx", 21, "/images/profiles/nebulynx.png", 6, 4, 5, "Let’s dissolve boundaries—literally."},
                {"b103adf7-e0e0-44a2-95a0-1a34cb697cf2", "grix_of_tau", 88, "/images/profiles/grix-of-tau.png", 13, 1, 6, "My love language is plasma exchange."},
                {"5ebc1b6e-75f2-4ff5-a89f-fc672e34c7d4", "void_seraph", 42, "/images/profiles/void-seraph.png", 1, 3, 2, "Cosmic cuddles > terrestrial troubles."},
                {"109d1a74-87fd-4622-b4a6-275b2d69e05e", "rixxa_ray", 27, "/images/profiles/rixxa-ray.png", 5, 7, 4, "Will serenade you with gamma waves."},
                {"04c63be3-e571-4b7f-82a7-fdb9b80b243c", "blen_znar", 65, "/images/profiles/blen-znar.png", 7, 6, 3, "Seeking soulmate for shared sporeclouds."},
                {"8a16cb9c-e1ee-4b4a-8cf6-2f792a77d646", "kaiju_dreams", 39, "/images/profiles/kaiju-dreams.png", 14, 10, 1, "Big hugs. Bigger footprints."},
                {"c3f1e5e3-b093-4d47-8677-6b35f04b5cd2", "eldra_synth", 31, "/images/profiles/eldra-synth.png", 3, 8, 6, "Bio-synthetic, emotionally authentic."},
                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", "plume_prax", 58, "/images/profiles/plume-prax.png", 10, 2, 2, "Feathered fun from Nebula 9."},
                {"c9be9053-3516-49f8-ae4f-63ad1c6ea755", "driftunit7", 25, "/images/profiles/driftunit7.png", 8, 5, 5, "Powered by stardust and affection."},
                {"fa04f79f-6c26-45cc-961b-79c062c3b8e0", "slorba_fizz", 112, "/images/profiles/slorba-fizz.png", 9, 1, 2, "Retired space pirate, still charming."},
                {"0e76a0c6-15d4-4df2-bde0-dca35a04a909", "phelxia_z", 33, "/images/profiles/phelxia-z.png", 12, 7, 6, "Just a glitch in your love algorithm."},
                {"7867ad2f-4d62-4e7f-b99d-efb7b49a08b5", "orbix_void", 47, "/images/profiles/orbix-void.png", 15, 6, 1, "Floating alone in orbit... until you."},
                {"7b78cd1c-4bd1-44e0-8134-86a10f42103a", "gellynquor", 19, "/images/profiles/gellynquor.png", 3, 10, 3, "Ooze-forward personality, squish-compatible."},
                {"a20896fc-0e8f-496c-926e-ec92d3429c47", "xarnyx_omega", 72, "/images/profiles/xarnyx-omega.png", 6, 4, 4, "Galactic elder, young at core."},
                {"c6f5d2a3-4e92-4493-a4f5-cb5cc070c75e", "nova_cyra", 28, "/images/profiles/nova-cyra.png", 11, 3, 2, "Quantum entanglement? Let’s try it."},
                {"5c52b396-81d4-4137-a10b-4a3520c17282", "threx_blink", 36, "/images/profiles/threx-blink.png", 2, 9, 5, "Love me for my phase shifts."},
                {"e1d1c14c-cb9e-46c7-9e23-35e11802df56", "glimbo_frax", 101, "/images/profiles/glimbo-frax.png", 5, 8, 6, "Veteran of 12 love wars. Ready for peace."},


                {"8f94a0b9-2bd9-4c82-8d3b-589f55b0c06f", "ZentharaPrime", 45, "/images/profiles/zenthara-prime.png", 7, 2, 1, "Searching for a mind to meld with."},
                {"bc4e3c58-2384-4492-bbb6-ccfc7c93985e", "gRyx-121", 132, "/images/profiles/gryx-121.png", 3, 8, 5, "Bioluminescence is my love language."},
                {"f7b1e05e-3963-40a4-a47f-fb82e9d9e71d", "OrbaLynx", 23, "/images/profiles/orbalynx.png", 14, 6, 2, "Hyperjump into my heart."},
                {"0f147ecf-5e3d-413b-a512-d46e44ed0b02", "velk.nara", 61, "/images/profiles/velk-nara.png", 9, 1, 4, "Emotionally open. Physically gelatinous."},
                {"f1e91259-96be-4aa5-92e2-6cc3abddfd9e", "TX-ZetaVibe", 35, "/images/profiles/tx-zetavibe.png", 5, 10, 3, "Thrives on sonic empathy."},
                {"4d7b300f-7bb3-4f25-9a49-dfaab5094e6e", "CryonDust", 70, "/images/profiles/cryon-dust.png", 8, 9, 6, "Frozen heart, warming up for you."},
                {"9d218ed0-70f7-4db8-973e-fb04298d8edc", "nimbari.AXO", 22, "/images/profiles/nimbari-axo.png", 1, 3, 1, "Dreams in UV. Seeks same."},
                {"19d9e92a-24fc-4bd5-9f02-882f3f4a5de9", "EchoZee99", 48, "/images/profiles/echozee99.png", 13, 4, 2, "Holographic soul, analog love."},
                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", "Dr-Xorb", 109, "/images/profiles/dr-xorb.png", 10, 7, 4, "Retired from war, ready for romance."},
                {"2b2a4cf1-7c0c-43e6-ae6b-7292d0b6a219", "vexaNova", 30, "/images/profiles/vexanova.png", 2, 2, 3, "Craves conversations across timelines."},
                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", "warp.singer", 27, "/images/profiles/warp-singer.png", 4, 5, 5, "Broadcasting affection on subspace."},
                {"d9e0b40f-1654-4ec0-a12a-3069b2b3bafd", "Glimm88", 56, "/images/profiles/glimm88.png", 12, 10, 6, "Oozing charisma (and other things)."},
                {"cdb5f6d5-6026-4725-baf3-18903b250dcb", "Lulon-Kree", 33, "/images/profiles/lulon-kree.png", 11, 6, 2, "Multidimensional thinker, mono-partner lover."},
                {"10a0b3b2-28e4-45b6-8887-44b75c20a391", "x.xelantra", 26, "/images/profiles/x-xelantra.png", 15, 9, 5, "Streaming pheromones and poetry."},
                {"f4cf46cb-84db-4b1e-97ad-94844b6c6c6a", "QuorviusRex", 84, "/images/profiles/quorviusrex.png", 6, 8, 1, "Royal blood, humble heart."},
                {"54dd44f7-d65a-4328-ae36-86a04bb366f0", "myrrh_112", 40, "/images/profiles/myrrh-112.png", 7, 1, 4, "Sings to moons. Might sing to you."},
                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", "Proto-Jean", 19, "/images/profiles/proto-jean.png", 5, 3, 6, "Not just a prototype anymore."},
                {"ca8a64b9-d615-423e-b99a-d1a0dfd30217", "Zar.Tekk", 52, "/images/profiles/zar-tekk.png", 9, 4, 3, "Low-orbit cuddles? Yes please."},
                {"bdd5c771-e029-47d2-a058-0fc2f62cc3ce", "SkarnyxTheThird", 120, "/images/profiles/skarnyx-thethird.png", 3, 7, 5, "Heir to the throne of affection."},
                {"c0dfe381-e7f7-4d60-b771-d6c680e6b3ae", "ViRa_Nox", 38, "/images/profiles/vira-nox.png", 1, 10, 2, "Dark matter in the streets, radiant in the sheets."},

                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", "ThrayneVale", 102, "/images/profiles/thrayne-vale.png", 2, 7, 6, "Once a star-navigator, now seeking emotional constellations. I believe every heart has its own orbit."},
                {"a7ed03cf-9ec5-4f0a-a5b6-ec51d0381b2a", "mox.vikta", 26, "/images/profiles/mox-vikta.png", 9, 4, 1, "Bio-alchemist turned romantic adventurer. I love translating pheromones into poetry."},
                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", "Jexa-Dune", 33, "/images/profiles/jexa-dune.png", 4, 5, 2, "Explorer of forgotten moons and meaningful eye contact. I’m here to build a universe for two."},
                {"b0b1ac73-f21e-4684-9f3a-8f2aa6a2e5b6", "tullynox", 46, "/images/profiles/tullynox.png", 12, 3, 5, "Emotionally available, physically translucent. I believe love should be both infinite and weird."},
                {"dfe81c49-60dc-4e1a-8208-26779f4f26a7", "ZEPHA-RUNE", 137, "/images/profiles/zepha-rune.png", 5, 10, 3, "Wanderer of wormholes and occasional karaoke performer. Ready to sync heartbeats across timelines."},
                {"d620ad8e-d7c4-4bcb-8514-29b27e9b2f62", "krill3x", 21, "/images/profiles/krill3x.png", 8, 1, 4, "Just a cluster of dreams inside an exosuit. Looking for someone to rewrite destiny with."},
                {"1e985019-4b55-4d8b-91a5-11d460d41610", "Xenray.Blossom", 58, "/images/profiles/xenray-blossom.png", 11, 8, 2, "Grew up on a crystalline asteroid and still sparkle under pressure. Connection matters more to me than molecular structure."},
                {"a741dd32-5f2e-4fa2-b16a-67b1522596e1", "ArloNebulon", 39, "/images/profiles/arlo-nebulon.png", 3, 2, 1, "Currently orbiting Saturn's third ring, but open to relocation. Let's build gravity together."},
                {"49f9cfc9-34e2-4d47-913c-1d9a0ecaf3ee", "ravax_six", 70, "/images/profiles/ravax-six.png", 14, 6, 5, "A mind forged in fusion reactors, but a heart as soft as stardust. Let’s dissolve into the quantum foam together."},
                {"3a037c99-69be-4537-9a4b-81d55c82b816", "glim-dot-x", 18, "/images/profiles/glim-dot-x.png", 1, 9, 2, "The youngest satellite in the system, but wise beyond lightyears. Seeking someone to teach me slow dancing in zero-G."},
                {"8d0cb94c-f9d3-46ed-bf2c-3d8a7fd13a4c", "Vyrinox", 112, "/images/profiles/vyrinox.png", 10, 5, 6, "Retired neural network whisperer. Still open to high-bandwidth emotional connection."},
                {"372edabe-1a3d-4b99-92ff-58d4be2b2b2a", "Nova-Belle", 28, "/images/profiles/nova-belle.png", 15, 7, 3, "I was raised by cosmic whales and learned empathy from the tides of space. Let’s create harmony among chaos."},
                {"b0b30e5a-4d2c-4579-a5a4-7500a3913483", "zog_theBold", 52, "/images/profiles/zog-thebold.png", 6, 1, 1, "Once a warrior, now a gardener of emotion. Seeking peace and passionate parallel lives."},
                {"f1c83b3a-47e3-4558-8b3d-8c84b5608026", "NixiJett", 35, "/images/profiles/nixijett.png", 2, 4, 4, "I drive a comet like it’s a cruiser. Swipe right if you can handle speed and soul."},
                {"e91b2c1e-b982-4e50-9c35-7c79c682df64", "Omma.quinn", 61, "/images/profiles/omma-quinn.png", 7, 10, 2, "Polyphonic communicator with a voice for every mood. Searching for symphony, not solo acts."},
                {"82a4d4bc-cd96-4f90-bc6e-f33e8a274e45", "biolux.T", 29, "/images/profiles/biolux-t.png", 13, 2, 3, "My glow intensifies when I’m happy. Let's create a lightshow the galaxies will envy."},
                {"cdd6d718-1edc-4a55-985b-e81a51fba650", "Yenna-Mir", 44, "/images/profiles/yenna-mir.png", 6, 3, 5, "I sculpt dreams from planetary rings. Looking for someone who sees the art in the stars."},
                {"cb3f4b0f-f9e7-4fc5-a0b6-f546ed4e6bb3", "zelpoKlaw", 89, "/images/profiles/zelpoklaw.png", 9, 6, 2, "I have survived nine singularities and one breakup. This time, I'm looking for something timeless."},
                {"ac4170b7-55cf-446e-861b-17f45213e839", "Rin.Ox", 24, "/images/profiles/rin-ox.png", 1, 10, 1, "Small, speedy, and sweet like a rogue asteroid. Swipe if you like surprises and starlit mischief."},
                {"7bfeeb47-4c4b-4db0-8d2d-1b4c33238f18", "gremmaVox", 37, "/images/profiles/gremma-vox.png", 11, 8, 6, "I speak twelve love dialects and dream in soundwaves. If you hum, I’ll harmonize."}
        };

        for (Object[] user : users) {
            UUID id = UUID.fromString((String) user[0]);  // Convert first element to UUID

            jdbcTemplate.update(
                    "INSERT INTO users (id, username, age, profilepic_src, homeplanet_id, bodyform_id, looking_for_id, bio) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
                            + "ON CONFLICT (id) DO NOTHING",
                    id,                   // UUID instead of String
                    user[1],              // username
                    user[2],              // age
                    user[3],              // profilepic_src
                    user[4],              // homeplanet_id
                    user[5],              // bodyform_id
                    user[6],              // looking_for_id
                    user[7]               // bio
            );
        }
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
            {"0b2e39ea-d1ef-4b6c-93b6-1d0ff861197e", 12},

                {"e2d1c9f6-758f-4e5f-b2a4-249d7b09aa62", 23},
                {"6b9f12d4-9245-4634-a8b7-3aa0c1eae7fc", 7},
                {"ae2a1129-0d15-4fa4-bdb4-fd4893a6d9b9", 34},
                {"b0d105c1-48ef-4bba-8b52-d4d4e338e25f", 12},
                {"43f82453-8aaf-4b3a-83f9-8e56c5ac1779", 29},
                {"13fd2ec7-e1f1-462e-bae7-404ae8a5151f", 5},
                {"d5d92180-5041-48aa-bbc7-d5d2274cb192", 18},
                {"e7f153bd-fb89-4959-8d5b-798b7d7298e5", 40},
                {"f3b6e36f-e4e2-4983-a2c5-d51cbf6f2a94", 15},
                {"a4b63de0-42cb-44ae-9251-7982355a1574", 31},
                {"b5fe450c-1db5-4643-95ea-8bb2dd2822a1", 9},
                {"da8c5916-dc38-40f0-a5bb-6590aa06650c", 26},
                {"f0e27dd2-7fc6-4e56-a4fa-3bc00f1ad70e", 3},
                {"cf1eeb2a-e816-4982-a34f-2b29c7721d96", 37},
                {"7ce6d285-1b27-466e-a06f-31596efeb317", 14},
                {"379e84b3-df3d-4fae-832e-98f92e9732a5", 22},
                {"be0bd7df-8488-4c6f-b3cf-379c78f9a52b", 6},
                {"c83b4dc9-2e49-4b9a-800f-929b5f2187b0", 33},
                {"44fd1dc6-fcf3-4e49-87e9-d9c8aa1be265", 19},
                {"c66b0f7b-7484-4f2e-83cb-254019f2fe9e", 28},
                {"37c5b0f4-95b8-4d44-9e9a-f6d36fd3a6ef", 11},
                {"ea65a3c0-3bb1-45e5-99c3-5c79bdc07d2e", 38},
                {"d1f79fd5-5c6e-4375-86c7-135d5d718aef", 2},
                {"a85b437c-c3e6-40cd-a8f4-c3c9fbe1deed", 25},
                {"b103adf7-e0e0-44a2-95a0-1a34cb697cf2", 16},
                {"5ebc1b6e-75f2-4ff5-a89f-fc672e34c7d4", 30},
                {"109d1a74-87fd-4622-b4a6-275b2d69e05e", 8},
                {"04c63be3-e571-4b7f-82a7-fdb9b80b243c", 35},
                {"8a16cb9c-e1ee-4b4a-8cf6-2f792a77d646", 20},
                {"c3f1e5e3-b093-4d47-8677-6b35f04b5cd2", 13},

                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", 17},
                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", 16},
                {"a21e94e4-8ed0-41c4-a7bb-273d30f1e1aa", 31},
                {"c9be9053-3516-49f8-ae4f-63ad1c6ea755", 24},
                {"fa04f79f-6c26-45cc-961b-79c062c3b8e0", 4},
                {"fa04f79f-6c26-45cc-961b-79c062c3b8e0", 10},
                {"0e76a0c6-15d4-4df2-bde0-dca35a04a909", 36},
                {"0e76a0c6-15d4-4df2-bde0-dca35a04a909", 32},
                {"7867ad2f-4d62-4e7f-b99d-efb7b49a08b5", 10},
                {"7867ad2f-4d62-4e7f-b99d-efb7b49a08b5", 9},
                {"7b78cd1c-4bd1-44e0-8134-86a10f42103a", 27},
                {"a20896fc-0e8f-496c-926e-ec92d3429c47", 1},
                {"a20896fc-0e8f-496c-926e-ec92d3429c47", 40},
                {"c6f5d2a3-4e92-4493-a4f5-cb5cc070c75e", 39},
                {"5c52b396-81d4-4137-a10b-4a3520c17282", 21},
                {"e1d1c14c-cb9e-46c7-9e23-35e11802df56", 8},
                {"8f94a0b9-2bd9-4c82-8d3b-589f55b0c06f", 32},
                {"bc4e3c58-2384-4492-bbb6-ccfc7c93985e", 15},
                {"f7b1e05e-3963-40a4-a47f-fb82e9d9e71d", 29},
                {"0f147ecf-5e3d-413b-a512-d46e44ed0b02", 6},
                {"f1e91259-96be-4aa5-92e2-6cc3abddfd9e", 18},
                {"f1e91259-96be-4aa5-92e2-6cc3abddfd9e", 15},
                {"4d7b300f-7bb3-4f25-9a49-dfaab5094e6e", 35},
                {"9d218ed0-70f7-4db8-973e-fb04298d8edc", 12},
                {"19d9e92a-24fc-4bd5-9f02-882f3f4a5de9", 40},
                {"19d9e92a-24fc-4bd5-9f02-882f3f4a5de9", 8},
                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", 3},
                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", 17},
                {"55f47c3e-b1f2-4a2e-8d33-9b0a75cb9aaf", 9},
                {"2b2a4cf1-7c0c-43e6-ae6b-7292d0b6a219", 26},
                {"2b2a4cf1-7c0c-43e6-ae6b-7292d0b6a219", 27},
                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 9},
                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 10},
                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 14},
                {"6728f9b1-54cb-4f1d-9443-9e40e1904913", 19},
                {"d9e0b40f-1654-4ec0-a12a-3069b2b3bafd", 22},
                {"cdb5f6d5-6026-4725-baf3-18903b250dcb", 14},
                {"10a0b3b2-28e4-45b6-8887-44b75c20a391", 31},
                {"f4cf46cb-84db-4b1e-97ad-94844b6c6c6a", 7},
                {"f4cf46cb-84db-4b1e-97ad-94844b6c6c6a", 8},
                {"54dd44f7-d65a-4328-ae36-86a04bb366f0", 38},
                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 19},
                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 19},
                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 12},
                {"a3c479cb-58b3-438c-b8ee-c59d97820e70", 15},
                {"ca8a64b9-d615-423e-b99a-d1a0dfd30217", 25},
                {"bdd5c771-e029-47d2-a058-0fc2f62cc3ce", 11},
                {"bdd5c771-e029-47d2-a058-0fc2f62cc3ce", 10},
                {"c0dfe381-e7f7-4d60-b771-d6c680e6b3ae", 33},
                {"c0dfe381-e7f7-4d60-b771-d6c680e6b3ae", 37},

                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 16},
                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 15},
                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 12},
                {"1aa3c2b7-4b42-49a0-85f1-31a1e558c8dc", 11},
                {"a7ed03cf-9ec5-4f0a-a5b6-ec51d0381b2a", 28},
                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", 5},
                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", 34},
                {"ed6f0d3a-657f-40da-8b5e-497706b5503b", 32},
                {"b0b1ac73-f21e-4684-9f3a-8f2aa6a2e5b6", 34},
                {"dfe81c49-60dc-4e1a-8208-26779f4f26a7", 13},
                {"d620ad8e-d7c4-4bcb-8514-29b27e9b2f62", 37},
                {"1e985019-4b55-4d8b-91a5-11d460d41610", 2},
                {"a741dd32-5f2e-4fa2-b16a-67b1522596e1", 23},
                {"49f9cfc9-34e2-4d47-913c-1d9a0ecaf3ee", 30},
                {"3a037c99-69be-4537-9a4b-81d55c82b816", 9},
                {"8d0cb94c-f9d3-46ed-bf2c-3d8a7fd13a4c", 20},
                {"372edabe-1a3d-4b99-92ff-58d4be2b2b2a", 40},
                {"b0b30e5a-4d2c-4579-a5a4-7500a3913483", 11},
                {"f1c83b3a-47e3-4558-8b3d-8c84b5608026", 27},
                {"e91b2c1e-b982-4e50-9c35-7c79c682df64", 4},
                {"82a4d4bc-cd96-4f90-bc6e-f33e8a274e45", 36},
                {"cdd6d718-1edc-4a55-985b-e81a51fba650", 18},
                {"cb3f4b0f-f9e7-4fc5-a0b6-f546ed4e6bb3", 25},
                {"ac4170b7-55cf-446e-861b-17f45213e839", 7},
                {"7bfeeb47-4c4b-4db0-8d2d-1b4c33238f18", 32},


        };

        for (Object[] userInterest : userInterests) {
            UUID userId = UUID.fromString((String) userInterest[0]);  // Convert string to UUID
            Integer interestId = (Integer) userInterest[1];            // Interest ID remains an Integer

            jdbcTemplate.update(
                    "INSERT INTO user_interests (user_id, interest_id) VALUES (?, ?) ON CONFLICT (user_id, interest_id) DO NOTHING",
                    userId, interestId  // Pass UUID and Integer to the query
            );
        }
        System.out.println("User interests seeded");
    }

    private void seedBodyforms() {
        for (Bodyforms bodyform : Bodyforms.values()) {
            jdbcTemplate.update(
                    "INSERT INTO bodyforms (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
                    bodyform.Code(),
                    bodyform.DisplayName()
            );
        }

        System.out.println("Bodyforms seeded");
    }

    private void seedHomeplanets() {
        for (Planets planet : Planets.values()) {
            jdbcTemplate.update(
                    "INSERT INTO homeplanets (id, name, latitude, longitude) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO NOTHING",
                    planet.Code(),
                    planet.DisplayName(),
                    planet.Latitude(),
                    planet.Longitude()
            );
        }

        System.out.println("Homeplanets seeded");
    }

    private void seedLookingFor() {
        for (LookingFor lf : LookingFor.values()) {
            jdbcTemplate.update(
                    "INSERT INTO looking_for (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
                    lf.Code(),
                    lf.DisplayName()
            );
        }

        System.out.println("Looking_for seeded");
    }

    private void seedInterests() {
        for (Interests interest : Interests.values()) {
            jdbcTemplate.update(
                    "INSERT INTO interests (id, interest) VALUES (?, ?) ON CONFLICT (id) DO NOTHING",
                    interest.Code(),
                    interest.DisplayName()
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
