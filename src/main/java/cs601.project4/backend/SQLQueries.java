package cs601.project4.backend;

import java.util.Map;

public class SQLQueries {
    public static Map<String, String> userQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO users (name, email) VALUES (?, ?)"),
            Map.entry("SELECT_BY_EMAIL",  "SELECT * FROM users WHERE email=?"),
            Map.entry("UPDATE",  "UPDATE users SET name=?, email=? WHERE id=?")
    );
    public static Map<String, String> transactionQueries = Map.ofEntries(
            Map.entry("SELECT_BY_USER_ID",  "SELECT event_name, transaction_type, users.name AS other_user FROM transactions JOIN events on transactions.event_id = events.id LEFT JOIN users on transactions.other_user_id = users.id WHERE user_id=?"),
            Map.entry("PURCHASE_TICKET",  "INSERT INTO transactions (user_id, event_id, transaction_type) VALUES (?, ?, 'purchase')"),
            Map.entry("TRANSFER_TICKET",  "INSERT INTO transactions (user_id, event_id, other_user_id, transaction_type) VALUES (?, ?, ?, 'transfer')")
    );
    public static Map<String, String> eventQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO events (event_name, created_by, available, purchased) VALUES (?, ?, ?, 0)"),
            Map.entry("SELECT",  "SELECT * FROM events WHERE id=?"),
            Map.entry("UPDATE",  "UPDATE events SET available=?, purchased=? WHERE id=?"),
            Map.entry("SELECT_ALL",  "SELECT * FROM events"),
            Map.entry("SELECT_ALL_WITH_USERS",  "SELECT events.id, event_name, name AS user_name, available, purchased FROM events JOIN users ON events.created_by = users.id")
    );
}
