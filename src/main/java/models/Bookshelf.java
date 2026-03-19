package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Bookshelf {
    private int id;
    private int userId;
    private LocalDateTime addedAt;

    public Bookshelf() {}

    public Bookshelf(int id, int userId, LocalDateTime addedAt) {
        this.id = id;
        this.userId = userId;
        this.addedAt = addedAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt.toLocalDateTime();
    }
}
