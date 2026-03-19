package models;

import java.sql.Date;

public class Review extends Base {
    private int userId;
    private int ebookId;
    private int rating;
    private String comment;
    private Date createdAt;

    public Review(int id) {
        super(id);
    }

    public Review(int id, int userID, int ebookID, int rating, String comment, Date createdAt) {
        super(id);
        this.userId = userID;
        this.ebookId = ebookID;
        this.rating = rating;
        this.comment = comment;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
