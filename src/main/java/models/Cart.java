package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Cart extends Base {
    private int userID;
    private Timestamp createdAt;

    public Cart(int id) {
        super(id);
    }

    public Cart(int id, int userID, Timestamp createdAt) {
        super(id);
        this.userID = userID;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
