package models;

import java.io.Serializable;

public class Feedback extends Base implements Serializable {
    private int userID;
    private String message;
    private int status;

    public Feedback(int id) {
        super(id);
    }

    public Feedback(int id, int userID, String message, int status) {
        super(id);
        this.userID = userID;
        this.message = message;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }
}
