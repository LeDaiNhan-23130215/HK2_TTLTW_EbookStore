package models;

import java.sql.Timestamp;

public class PasswordReset extends Base {
    private int userID;
    private String tokenHash;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private boolean isUsed;

    public PasswordReset(int id) {
        super(id);
    }

    // getter & setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Timestamp expiresAt) { this.expiresAt = expiresAt; }

    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
}
