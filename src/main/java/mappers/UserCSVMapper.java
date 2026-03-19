package mappers;

import models.User;

public class UserCSVMapper implements CSVMapper<User>{
    @Override
    public User map(String[] cols) {
        String name = cols.length > 0 ? cols[0].trim() : "";
        String email = cols.length > 1 ? cols[1].trim() : "";
        String phoneNumber = cols.length > 2 ? cols[2].trim() : "";
        String password = cols.length > 3 ? cols[3].trim() : "";
        String role = cols.length > 4 ? cols[4].trim() : "";

        if (name.isEmpty()) return null;

        return new User(name, email, phoneNumber, password, role);
    }
}
