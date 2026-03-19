package models;

import java.time.LocalDateTime;

public class Wishlist extends Base {
    private int userID;
    private LocalDateTime createdAt;

    public Wishlist(int id) {
        super(id);
    }

    public Wishlist(int id, int userID, LocalDateTime createdAt) {
        super(id);
        this.userID = userID;
        this.createdAt = createdAt;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
