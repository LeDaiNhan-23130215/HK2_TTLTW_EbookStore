package models;

public class User extends Base {
    private String username;
    private String email;
    private String phoneNum;
    private String password;
    private String role;

    public User(int id, String username, String email, String phoneNum, String password, String role) {
        super(id);
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
        this.role = role;
    }

    public User(int id) {
        super(id);
    }

    public User(String username, String email, String phoneNum, String password, String role) {
        super(-1);
        this.username = username;
        this.email = email;
        this.phoneNum = phoneNum;
        this.password = password;
        this.role = role;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
