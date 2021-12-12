package cs601.project4.backend;

import java.util.Map;

public class SQLQueries {
    public static Map<String, String> userQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO users (name, email) VALUES (?, ?)"),
            Map.entry("SELECT_BY_EMAIL",  "SELECT * FROM users WHERE email=?"),
            Map.entry("SELECT_BY_ID",  "SELECT * FROM users WHERE id=?"),
            Map.entry("UPDATE",  "UPDATE users SET name=?, email=? WHERE id=?")
    );
    public static Map<String, String> transactionQueries = Map.ofEntries(
            Map.entry("SELECT_BY_USER_ID",  "SELECT events.id AS event_id, event_name, transaction_type, users.name AS other_user FROM transactions JOIN events on transactions.event_id = events.id LEFT JOIN users on transactions.other_user_id = users.id WHERE user_id=?"),
            Map.entry("PURCHASE_TICKET",  "INSERT INTO transactions (user_id, event_id, transaction_type) VALUES (?, ?, 'purchase')"),
            Map.entry("TRANSFER_TICKET",  "INSERT INTO transactions (user_id, event_id, other_user_id, transaction_type) VALUES (?, ?, ?, 'transfer')"),
            Map.entry("MY_TICKETS",  "WITH pur AS (SELECT event_id, COUNT(*) AS cp FROM transactions WHERE user_id=? AND transaction_type='purchase' GROUP BY event_id), tr AS (SELECT event_id, COUNT(*) AS ct FROM transactions WHERE user_id=? AND transaction_type='transfer' GROUP BY event_id), rec AS (SELECT event_id, COUNT(*) AS cr FROM transactions WHERE other_user_id=? AND transaction_type='transfer' GROUP BY event_id), total_counts AS (SELECT cp + cr - ct AS total, event_id FROM pur FULL OUTER JOIN tr ON tr.event_id = pur.event_id FULL OUTER JOIN rec ON rec.event_id = tr.event_id) SELECT events.event_name, event_id FROM total_counts JOIN events ON events.id=transactions.event_id where total > 0")
    );
    public static Map<String, String> eventQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO events (event_name, created_by, description, available, purchased) VALUES (?, ?, ?, ?, 0)"),
            Map.entry("SELECT",  "SELECT * FROM events WHERE id=?"),
            Map.entry("UPDATE",  "UPDATE events SET event_name=?, available=?, purchased=? WHERE id=?"),
            Map.entry("UPDATE_TICKETS",  "UPDATE events SET available=?, purchased=? WHERE id=?"),
            Map.entry("DELETE",  "DELETE FROM events WHERE id=?"),
            Map.entry("SELECT_ALL_WITH_USERS",  "SELECT events.id, event_name, description, name AS user_name, available, purchased FROM events JOIN users ON events.created_by = users.id"),
            Map.entry("SEARCH",  "SELECT events.id, event_name, description, name AS user_name, available, purchased FROM events JOIN users ON events.created_by = users.id WHERE (event_name LIKE ? OR description LIKE ?) AND name LIKE ? AND available LIKE ? AND purchased LIKE ?")
            );
    public static Map<String, String> userTicketsQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO user_tickets (user_id, event_id) VALUES (?, ?)"),
            Map.entry("DELETE",  "DELETE FROM user_tickets WHERE user_id=? AND event_id=? LIMIT 1"),
            Map.entry("SELECT",  "SELECT event_name, event_id FROM user_tickets JOIN events on events.id = user_tickets.event_id WHERE user_id=?")
    );
}
