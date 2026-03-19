package DTO;

import java.sql.Timestamp;

public class FeedbackAdminView {
    private int id;
    private int userID;
    private String username;
    private String email;
    private String message;
    private Timestamp createdAt;
    private int status;

    public FeedbackAdminView(int id, int userID, String username, String email,
                             String message, Timestamp createdAt, int status) {
        this.id = id;
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
    }

    // getter only (view object)
    public int getId() { return id; }
    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public Timestamp getCreatedAt() { return createdAt; }
    public int getStatus() { return status; }
}
