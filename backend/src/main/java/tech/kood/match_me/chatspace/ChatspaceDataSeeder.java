package tech.kood.match_me.chatspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class ChatspaceDataSeeder implements CommandLineRunner {

    @Autowired
    @Qualifier("chatspaceJdbcTemplate") // Adjust qualifier name as needed
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== SEEDING CHATSPACE DATA ===");

        // First, fix the table structure and sequences
        createSequencesAndFixTables();

        // Then seed data in proper order
        seedAllData();

        System.out.println("=== CHATSPACE DATA SEEDING COMPLETE ===");
    }

    private void createSequencesAndFixTables() {
        Map<String, String> columnTypes = Map.of(
                "users", "uuid",
                "conversations", "uuid",
                "messages", "uuid"
                // Add more tables here as you provide the models
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

    private void seedAllData() throws SQLException {
        // Seed data in dependency order
        seedUsers();
        seedConversations();
        seedConversationParticipants();
        seedUserConnections();
        seedMessages();

        verifyData();
    }

    private void seedUsers() throws SQLException {
        Object[][] users = {
                {"11111111-1111-1111-1111-111111111111", "alice_space", "ONLINE", "/images/profiles/starhopper.png"},
                {"22222222-2222-2222-2222-222222222222", "bob_cosmic", "OFFLINE", "/images/profiles/nebular-nikki.png"},
                {"33333333-3333-3333-3333-333333333333", "charlie_stellar", "OFFLINE", "/images/profiles/cosmic-joe.png"}
        };

        for (Object[] user : users) {
            UUID id = UUID.fromString((String) user[0]);


            try {
                jdbcTemplate.update(
                        "INSERT INTO users (id, username, status, lastactive, profile_pic_src)"
                                + " VALUES (?, ?, ?, ?, ?) "
                                + "ON CONFLICT (id) DO NOTHING",
                        id,
                        user[1],
                        user[2],
                        java.time.LocalDateTime.now().minusMinutes(10),
                        user[3]
                );
            } catch (Exception e) {
                System.out.println("Error seeding user " + id + ": " + e.getMessage());
            }
        }

        System.out.println("Users seeded");
    }

    private void seedConversations() {
        Object[][] conversations = {
                {"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", 1}, // 1 day ago
                {"bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", 2}  // 2 days ago
        };

        for (Object[] conversation : conversations) {
            UUID id = UUID.fromString((String) conversation[0]);
            int daysAgo = (Integer) conversation[1];

            LocalDateTime createdAt = java.time.LocalDateTime.now().minusDays(daysAgo);

            try {
                // Try different column name variations
                jdbcTemplate.update(
                        "INSERT INTO conversations (id, created_at, last_updated_at) VALUES (?, ?, ?) " +
                                "ON CONFLICT (id) DO NOTHING",
                        id,
                        createdAt,
                        createdAt // Initially, last_updated_at = created_at
                );
            } catch (Exception e) {

                try {
                    jdbcTemplate.update(
                            "INSERT INTO conversations (id, createdAt, lastUpdatedAt) VALUES (?, ?, ?) " +
                                    "ON CONFLICT (id) DO NOTHING",
                            id,
                            createdAt,
                            createdAt
                    );
                } catch (Exception e2) {
                    System.out.println("Error seeding conversation " + id + ": " + e2.getMessage());
                }
            }
        }

        System.out.println("Conversations seeded");
    }

    private void seedConversationParticipants() {
        Object[][] participants = {
                {"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "11111111-1111-1111-1111-111111111111"}, // alice_space in conversation A
                {"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "22222222-2222-2222-2222-222222222222"}, // bob_cosmic in conversation A
                {"bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", "22222222-2222-2222-2222-222222222222"}, // bob_cosmic in conversation B
                {"bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", "33333333-3333-3333-3333-333333333333"}  // charlie_stellar in conversation B
        };

        for (Object[] participant : participants) {
            UUID conversationId = UUID.fromString((String) participant[0]);
            UUID userId = UUID.fromString((String) participant[1]);

            try {
                jdbcTemplate.update(
                        "INSERT INTO conversation_participants (conversation_id, user_id) VALUES (?, ?) " +
                                "ON CONFLICT (conversation_id, user_id) DO NOTHING",
                        conversationId,
                        userId
                );
            } catch (Exception e) {
                System.out.println("Error seeding participant " + userId + " for conversation " + conversationId + ": " + e.getMessage());
            }
        }

        System.out.println("Conversation participants seeded");
    }

    private void seedMessages() {
        Object[][] messages = {
                // {messageId, conversationId, senderId, content, minutesAgo, status}
                {"aaaa1111-1111-1111-1111-aaaaaaaaaaaa", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "11111111-1111-1111-1111-111111111111", "Hey, how are you?", 9, "SENT"},
                {"aaaa2222-2222-2222-2222-aaaaaaaaaaaa", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "22222222-2222-2222-2222-222222222222", "I am good, thanks! You?", 8, "DELIVERED"},
                {"bbbb1111-1111-1111-1111-bbbbbbbbbbbb", "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", "22222222-2222-2222-2222-222222222222", "Hello, ready for the meeting?", 1440, "READ"}, // 1 day = 1440 minutes
                {"bbbb2222-2222-2222-2222-bbbbbbbbbbbb", "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb", "33333333-3333-3333-3333-333333333333", "Yes, I am joining now.", 1380, "READ"} // 23 hours = 1380 minutes
        };

        for (Object[] message : messages) {
            UUID messageId = UUID.fromString((String) message[0]);
            UUID conversationId = UUID.fromString((String) message[1]);
            UUID senderId = UUID.fromString((String) message[2]);
            String content = (String) message[3];
            int minutesAgo = (Integer) message[4];
            String status = (String) message[5];

            LocalDateTime timestamp = java.time.LocalDateTime.now().minusMinutes(minutesAgo);

            try {
                // Remove enum casting
                jdbcTemplate.update(
                        "INSERT INTO messages (id, conversation_id, sender_id, content, timestamp, status) " +
                                "VALUES (?, ?, ?, ?, ?, ?) " +
                                "ON CONFLICT (id) DO NOTHING",
                        messageId,
                        conversationId,
                        senderId,
                        content,
                        timestamp,
                        status // Remove ::message_status casting
                );
            } catch (Exception e) {
                System.out.println("Error seeding message " + messageId + ": " + e.getMessage());
            }
        }

        System.out.println("Messages seeded");
    }

    private void seedUserConnections() throws SQLException {
        Object[][] connections = {
                {
                        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), // id
                        UUID.fromString("11111111-1111-1111-1111-111111111111"), // user_id
                        UUID.fromString("22222222-2222-2222-2222-222222222222"), // connected_user_id
                        java.time.LocalDateTime.now().minusHours(1)                    // created_at
                },
                {
                        UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                        UUID.fromString("22222222-2222-2222-2222-222222222222"),
                        UUID.fromString("33333333-3333-3333-3333-333333333333"),
                        java.time.LocalDateTime.now().minusHours(2)
                }
        };

        for (Object[] conn : connections) {
            try {
                jdbcTemplate.update(
                        "INSERT INTO user_connections (id, user_id, connected_user_id, created_at) " +
                                "VALUES (?, ?, ?, ?) " +
                                "ON CONFLICT (id) DO NOTHING",
                        conn[0], conn[1], conn[2], conn[3]
                );
            } catch (Exception e) {
                System.out.println("Error seeding user_connection " + conn[0] + ": " + e.getMessage());
            }
        }

        System.out.println("User connections seeded");
    }

    private void verifyData() {
        String[] tables = {
                "users",
                "conversations",
                "conversation_participants",
                "messages"
        };

        for (String table : tables) {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                System.out.println("Table " + table + " has " + count + " rows");
            } catch (Exception e) {
                System.out.println("Error counting rows in " + table + ": " + e.getMessage());
            }
        }
    }
}