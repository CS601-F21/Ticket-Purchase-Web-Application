package cs601.project4.backend;

import java.util.Map;

public class SQLQueries {
    public static Map<String, String> userQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO users (name, email) VALUES (?, ?)"),
            Map.entry("SELECT_BY_EMAIL",  "SELECT * FROM users WHERE email=?")
    );
    public static Map<String, String> transactionQueries = Map.ofEntries(
            Map.entry("SELECT_BY_USER_ID",  "SELECT event_id, transaction, other_user_id FROM transactions WHERE user_id=?"),
            Map.entry("PURCHASE_TICKET",  "INSERT INTO transactions (user_id, event_id, transaction) VALUES (?, ?, purchase)"),
            Map.entry("TRANSFER_TICKET",  "INSERT INTO transactions (user_id, event_id, other_user_id, transaction) VALUES (?, ?, ?, purchase)")
    );
    public static Map<String, String> eventQueries = Map.ofEntries(
            Map.entry("INSERT",  "INSERT INTO events (event_name, created_by, available, purchased) VALUES (?, ?, ?, 0)"),
            Map.entry("SELECT",  "SELECT * FROM events WHERE id=?"),
            Map.entry("UPDATE",  "UPDATE events SET available=?, purchased=? WHERE id=?"),
            Map.entry("SELECT_ALL",  "SELECT * FROM events")
    );
}
