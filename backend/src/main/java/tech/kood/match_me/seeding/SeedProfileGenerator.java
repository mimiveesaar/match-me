package tech.kood.match_me.seeding;

import org.springframework.stereotype.Component;
import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;

import java.util.*;


@Component
public class SeedProfileGenerator {

        private static final int PROFILE_COUNT = 100;
        private static final int MAX_INTEREST_ID = 40;
        private final UserDTO[] seedUsers = SeedUserGenerator.generate();
        private final Random random = new Random();

        private final Object[][] profileDataArray = {
                        {"nebular_nikki1", 31, "/images/profiles/nebular-nikki.png", 2, 2, 2,
                                        "Dreaming beyond the stars."},
                        {"nebular_nikki2", 31, "/images/profiles/nebular-nikki.png", 2, 2, 2,
                                        "Dreaming beyond the stars."},
                        {"cosmicjoe", 35, "/images/profiles/cosmic-joe.png", 1, 1, 1,
                                        "Looking for a co-pilot in life."},
                        {"gravitygal", 29, "/images/profiles/gravitygal.png", 3, 3, 3,
                                        "I never fall... unless it’s for someone."},
                        {"andromedaboy", 22, "/images/profiles/andromedaboy.png", 2, 3, 3,
                                        "Just a guy with a telescope and big dreams."},
                        {"orionquest", 40, "/images/profiles/orionquest.png", 4, 4, 4,
                                        "Long-time traveler, short-time sleeper."},
                        {"venusian_vibe", 26, "/images/profiles/venusian_vibe.png", 5, 5, 5,
                                        "Bringing sunshine wherever I go."},
                        {"marsmaven", 33, "/images/profiles/marsmaven.png", 2, 1, 2,
                                        "Martian-born, Earth-raised, curiosity-fueled."},
                        {"astrochick", 28, "/images/profiles/astrochick.png", 6, 6, 6,
                                        "Rockets, robots, and romance."},
                        {"quantumquinn", 38, "/images/profiles/quantumquinn.png", 3, 3, 2,
                                        "Entangled in all the right ways."},
                        {"lunar_luke", 24, "/images/profiles/lunar_luke.png", 7, 1, 3,
                                        "Moon-walker in search of a dance partner."},
                        {"plutonianpixie", 30, "/images/profiles/plutonianpixie.png", 3, 3, 3,
                                        "Cold hands, warm heart, outer orbit dreams."},
                        {"galaxygazer", 36, "/images/profiles/galaxygazer.png", 8, 2, 6,
                                        "My telescope sees far, but I’m looking for someone near."},
                        {"astro_ari", 27, "/images/profiles/astro-ari.png", 2, 2, 2,
                                        "Martinis and meteor showers."},
                        {"jupiterjazz", 42, "/images/profiles/jupiterjazz.png", 9, 3, 2,
                                        "Rhythms of the cosmos, melodies of love."},
                        {"zenzara", 21, "/images/profiles/zenzara.png", 10, 3, 3,
                                        "Meditating in zero gravity."},
                        {"nova_nate", 34, "/images/profiles/nova-nate.png", 11, 4, 4,
                                        "Burn bright, love brighter."},
                        {"solarys", 29, "/images/profiles/solarys.png", 12, 5, 1,
                                        "Daylight dreamer, starlight lover."},
                        {"wormholewendy", 26, "/images/profiles/wormholewendy.png", 13, 6, 2,
                                        "Faster than light, slower to trust."},
                        {"kepler_kai", 37, "/images/profiles/kepler-kai.png", 14, 1, 3,
                                        "Mapping hearts like exoplanets."},

                        {"khebab", 47, "/images/profiles/khebab.png", 1, 2, 6,
                                        "47 in human years."},
                        {"goldie_brains", 121, "/images/profiles/goldie-brains.png", 2, 1, 5,
                                        "Knows 47 ways to win at chess but forgets her keys every day."},
                        {"gong", 107, "/images/profiles/gong.png", 3, 2, 4,
                                        "Speaks only in haikus and kitchen metaphors."},
                        {"Krik", 19, "/images/profiles/krik.png", 4, 3, 3,
                                        "I have a twin brother. He’s slightly less cool."},
                        {"Krak", 19, "/images/profiles/krak.png", 4, 3, 3,
                                        "I have a twin brother. He thinks he’s cooler."},
                        {"siip", 78, "/images/profiles/siip.png", 6, 5, 1,
                                        "Retired pirate, current sudoku champion."},
                        {"potaroo", 40, "/images/profiles/potaroo.png", 7, 2, 6,
                                        "Allergic to drama, addicted to hummus."},
                        {"loonToon", 150, "/images/profiles/loontoon.png", 8, 7, 3,
                                        "Still waiting for their cartoon royalty checks."},
                        {"Bünf", 32, "/images/profiles/bunf.png", 9, 8, 4,
                                        "Collects spoons. Not ironically."},
                        {"Grong", 79, "/images/profiles/grong.png", 10, 9, 5,
                                        "Built like a tank, cries during baking shows."},
                        {"LupiLuu", 80, "/images/profiles/lupiluu.png", 11, 10, 6,
                                        "Can out-sing a canary and out-nap a cat."},
                        {"Krox", 59, "/images/profiles/krox.png", 12, 9, 5,
                                        "Gym rat in the streets, poet in the tweets."},
                        {"Paxy", 67, "/images/profiles/paxy.png", 13, 8, 4,
                                        "Thinks space is cool, still afraid of elevators."},
                        {"Spongey", 68, "/images/profiles/spongey.png", 14, 7, 3,
                                        "Absorbs vibes and conspiracy theories."},
                        {"Seesamike", 33, "/images/profiles/seesamike.png", 15, 6, 2,
                                        "Knows the secret recipe, won’t tell a soul."},
                        {"Zobra", 99, "/images/profiles/zobra.png", 1, 5, 1,
                                        "Once won a staring contest with a statue."},
                        {"Cafu", 75, "/images/profiles/cafu.png", 2, 4, 2,
                                        "Retired time traveler. Not allowed near microwaves."},
                        {"Bermylla", 88, "/images/profiles/bermylla.png", 3, 3, 3,
                                        "Got lost in a hedge maze in 1973. Still not sure if out."},
                        {"Bebiku", 93, "/images/profiles/bebiku.png", 4, 2, 4,
                                        "Dances like everyone’s watching. And charges admission."},
                        {"Sepik", 29, "/images/profiles/sepik.png", 5, 1, 5,
                                        "Once sneezed and invented a new form of jazz."},

                        {"zorblax_77", 120, "/images/profiles/zorblax-77.png", 2, 5, 4,
                                        "Fluent in over 6,000 love dialects."},
                        {"luma_vyra", 29, "/images/profiles/luma-vyra.png", 11, 2, 1,
                                        "Seeks someone to harmonize neural waves."},
                        {"qorrin_xel", 54, "/images/profiles/qorrin-xel.png", 4, 9, 3,
                                        "Half-squid, all heart."},
                        {"nebulynx", 21, "/images/profiles/nebulynx.png", 6, 4, 5,
                                        "Let’s dissolve boundaries—literally."},
                        {"grix_of_tau", 88, "/images/profiles/grix-of-tau.png", 13, 1, 6,
                                        "My love language is plasma exchange."},
                        {"void_seraph", 42, "/images/profiles/void-seraph.png", 1, 3, 2,
                                        "Cosmic cuddles > terrestrial troubles."},
                        {"rixxa_ray", 27, "/images/profiles/rixxa-ray.png", 5, 7, 4,
                                        "Will serenade you with gamma waves."},
                        {"blen_znar", 65, "/images/profiles/blen-znar.png", 7, 6, 3,
                                        "Seeking soulmate for shared sporeclouds."},
                        {"kaiju_dreams", 39, "/images/profiles/kaiju-dreams.png", 14, 10, 1,
                                        "Big hugs. Bigger footprints."},
                        {"eldra_synth", 31, "/images/profiles/eldra-synth.png", 3, 8, 6,
                                        "Bio-synthetic, emotionally authentic."},
                        {"plume_prax", 58, "/images/profiles/plume-prax.png", 10, 2, 2,
                                        "Feathered fun from Nebula 9."},
                        {"driftunit7", 25, "/images/profiles/driftunit7.png", 8, 5, 5,
                                        "Powered by stardust and affection."},
                        {"slorba_fizz", 112, "/images/profiles/slorba-fizz.png", 9, 1, 2,
                                        "Retired space pirate, still charming."},
                        {"phelxia_z", 33, "/images/profiles/phelxia-z.png", 12, 7, 6,
                                        "Just a glitch in your love algorithm."},
                        {"orbix_void", 47, "/images/profiles/orbix-void.png", 15, 6, 1,
                                        "Floating alone in orbit... until you."},
                        {"gellynquor", 19, "/images/profiles/gellynquor.png", 3, 10, 3,
                                        "Ooze-forward personality, squish-compatible."},
                        {"xarnyx_omega", 72, "/images/profiles/xarnyx-omega.png", 6, 4, 4,
                                        "Galactic elder, young at core."},
                        {"nova_cyra", 28, "/images/profiles/nova-cyra.png", 11, 3, 2,
                                        "Quantum entanglement? Let’s try it."},
                        {"threx_blink", 36, "/images/profiles/threx-blink.png", 2, 9, 5,
                                        "Love me for my phase shifts."},
                        {"glimbo_frax", 101, "/images/profiles/glimbo-frax.png", 5, 8, 6,
                                        "Veteran of 12 love wars. Ready for peace."},

                        {"ZentharaPrime", 45, "/images/profiles/zenthara-prime.png", 7, 2, 1,
                                        "Searching for a mind to meld with."},
                        {"gRyx-121", 132, "/images/profiles/gryx-121.png", 3, 8, 5,
                                        "Bioluminescence is my love language."},
                        {"OrbaLynx", 23, "/images/profiles/orbalynx.png", 14, 6, 2,
                                        "Hyperjump into my heart."},
                        {"velk.nara", 61, "/images/profiles/velk-nara.png", 9, 1, 4,
                                        "Emotionally open. Physically gelatinous."},
                        {"TX-ZetaVibe", 35, "/images/profiles/tx-zetavibe.png", 5, 10, 3,
                                        "Thrives on sonic empathy."},
                        {"CryonDust", 70, "/images/profiles/cryon-dust.png", 8, 9, 6,
                                        "Frozen heart, warming up for you."},
                        {"nimbari.AXO", 22, "/images/profiles/nimbari-axo.png", 1, 3, 1,
                                        "Dreams in UV. Seeks same."},
                        {"EchoZee99", 48, "/images/profiles/echozee99.png", 13, 4, 2,
                                        "Holographic soul, analog love."},
                        {"Dr-Xorb", 109, "/images/profiles/dr-xorb.png", 10, 7, 4,
                                        "Retired from war, ready for romance."},
                        {"vexaNova", 30, "/images/profiles/vexanova.png", 2, 2, 3,
                                        "Craves conversations across timelines."},
                        {"warp.singer", 27, "/images/profiles/warp-singer.png", 4, 5, 5,
                                        "Broadcasting affection on subspace."},
                        {"Glimm88", 56, "/images/profiles/glimm88.png", 12, 10, 6,
                                        "Oozing charisma (and other things)."},
                        {"Lulon-Kree", 33, "/images/profiles/lulon-kree.png", 11, 6, 2,
                                        "Multidimensional thinker, mono-partner lover."},
                        {"x.xelantra", 26, "/images/profiles/x-xelantra.png", 15, 9, 5,
                                        "Streaming pheromones and poetry."},
                        {"QuorviusRex", 84, "/images/profiles/quorviusrex.png", 6, 8, 1,
                                        "Royal blood, humble heart."},
                        {"myrrh_112", 40, "/images/profiles/myrrh-112.png", 7, 1, 4,
                                        "Sings to moons. Might sing to you."},
                        {"Proto-Jean", 19, "/images/profiles/proto-jean.png", 5, 3, 6,
                                        "Not just a prototype anymore."},
                        {"Zar.Tekk", 52, "/images/profiles/zar-tekk.png", 9, 4, 3,
                                        "Low-orbit cuddles? Yes please."},
                        {"SkarnyxTheThird", 120, "/images/profiles/skarnyx-thethird.png", 3, 7, 5,
                                        "Heir to the throne of affection."},
                        {"ViRa_Nox", 38, "/images/profiles/vira-nox.png", 1, 10, 2,
                                        "Dark matter in the streets, radiant in the sheets."},

                        {"ThrayneVale", 102, "/images/profiles/thrayne-vale.png", 2, 7, 6,
                                        "Once a star-navigator, now seeking emotional constellations. I believe every heart has its own orbit."},
                        {"mox.vikta", 26, "/images/profiles/mox-vikta.png", 9, 4, 1,
                                        "Bio-alchemist turned romantic adventurer. I love translating pheromones into poetry."},
                        {"Jexa-Dune", 33, "/images/profiles/jexa-dune.png", 4, 5, 2,
                                        "Explorer of forgotten moons and meaningful eye contact. I’m here to build a universe for two."},
                        {"tullynox", 46, "/images/profiles/tullynox.png", 12, 3, 5,
                                        "Emotionally available, physically translucent. I believe love should be both infinite and weird."},
                        {"ZEPHA-RUNE", 137, "/images/profiles/zepha-rune.png", 5, 10, 3,
                                        "Wanderer of wormholes and occasional karaoke performer. Ready to sync heartbeats across timelines."},
                        {"krill3x", 21, "/images/profiles/krill3x.png", 8, 1, 4,
                                        "Just a cluster of dreams inside an exosuit. Looking for someone to rewrite destiny with."},
                        {"Xenray.Blossom", 58, "/images/profiles/xenray-blossom.png", 11, 8, 2,
                                        "Grew up on a crystalline asteroid and still sparkle under pressure. Connection matters more to me than molecular structure."},
                        {"ArloNebulon", 39, "/images/profiles/arlo-nebulon.png", 3, 2, 1,
                                        "Currently orbiting Saturn's third ring, but open to relocation. Let's build gravity together."},
                        {"ravax_six", 70, "/images/profiles/ravax-six.png", 14, 6, 5,
                                        "A mind forged in fusion reactors, but a heart as soft as stardust. Let’s dissolve into the quantum foam together."},
                        {"glim-dot-x", 18, "/images/profiles/glim-dot-x.png", 1, 9, 2,
                                        "The youngest satellite in the system, but wise beyond lightyears. Seeking someone to teach me slow dancing in zero-G."},
                        {"Vyrinox", 112, "/images/profiles/vyrinox.png", 10, 5, 6,
                                        "Retired neural network whisperer. Still open to high-bandwidth emotional connection."},
                        {"Nova-Belle", 28, "/images/profiles/nova-belle.png", 15, 7, 3,
                                        "I was raised by cosmic whales and learned empathy from the tides of space. Let’s create harmony among chaos."},
                        {"zog_theBold", 52, "/images/profiles/zog-thebold.png", 6, 1, 1,
                                        "Once a warrior, now a gardener of emotion. Seeking peace and passionate parallel lives."},
                        {"NixiJett", 35, "/images/profiles/nixijett.png", 2, 4, 4,
                                        "I drive a comet like it’s a cruiser. Swipe right if you can handle speed and soul."},
                        {"Omma.quinn", 61, "/images/profiles/omma-quinn.png", 7, 10, 2,
                                        "Polyphonic communicator with a voice for every mood. Searching for symphony, not solo acts."},
                        {"biolux.T", 29, "/images/profiles/biolux-t.png", 13, 2, 3,
                                        "My glow intensifies when I’m happy. Let's create a lightshow the galaxies will envy."},
                        {"Yenna-Mir", 44, "/images/profiles/yenna-mir.png", 6, 3, 5,
                                        "I sculpt dreams from planetary rings. Looking for someone who sees the art in the stars."},
                        {"zelpoKlaw", 89, "/images/profiles/zelpoklaw.png", 9, 6, 2,
                                        "I have survived nine singularities and one breakup. This time, I'm looking for something timeless."},
                        {"Rin.Ox", 24, "/images/profiles/rin-ox.png", 1, 10, 1,
                                        "Small, speedy, and sweet like a rogue asteroid. Swipe if you like surprises and starlit mischief."},
                        {"gremmaVox", 37, "/images/profiles/gremma-vox.png", 11, 8, 6,
                                        "I speak twelve love dialects and dream in soundwaves. If you hum, I’ll harmonize."}};


        public Map<String, List<Integer>> generateUserInterests() {
                Map<String, List<Integer>> userInterestMap = new HashMap<>();

                for (int i = 0; i < PROFILE_COUNT; i++) {
                        String userId = seedUsers[i].id().value().toString();
                        int interestCount = random.nextInt(8) + 1; // 1–8 interests
                        Set<Integer> interests = new HashSet<>();

                        while (interests.size() < interestCount) {
                                int interestId = random.nextInt(MAX_INTEREST_ID) + 1; // interest
                                                                                      // ids 1-40
                                interests.add(interestId);
                        }

                        userInterestMap.put(userId, new ArrayList<>(interests));
                }

                return userInterestMap;
        }


        public List<ProfileDTO> generateProfiles() {
                Map<String, List<Integer>> userInterestMap = generateUserInterests();
                List<ProfileDTO> profiles = new ArrayList<>();

                for (int i = 0; i < PROFILE_COUNT; i++) {
                        String userId = seedUsers[i].id().value().toString();
                        Object[] profileData = profileDataArray[i];

                        ProfileDTO profile = new ProfileDTO();
                        profile.setId(UUID.fromString(userId));
                        profile.setName((String) profileData[0]);
                        profile.setAge((Integer) profileData[1]);
                        profile.setProfilePic((String) profileData[2]);
                        profile.setHomeplanetId((Integer) profileData[3]);
                        profile.setBodyformId((Integer) profileData[4]);
                        profile.setLookingForId((Integer) profileData[5]);
                        profile.setBio((String) profileData[6]);

                        // Assign the interests
                        profile.setInterestIds(
                                        userInterestMap.getOrDefault(userId, new ArrayList<>()));

                        profiles.add(profile);
                }

                return profiles;
        }
};


