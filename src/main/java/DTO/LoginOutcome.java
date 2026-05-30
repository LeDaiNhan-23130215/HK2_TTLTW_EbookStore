package DTO;

import enums.LoginResult;
import models.User;

public class LoginOutcome {

    private final LoginResult result;
    private final User        user;

    private LoginOutcome(LoginResult result, User user) {
        this.result = result;
        this.user   = user;
    }


    public static LoginOutcome success(User user) {
        return new LoginOutcome(LoginResult.SUCCESS, user);
    }

    public static LoginOutcome of(LoginResult result) {
        return new LoginOutcome(result, null);
    }

    public LoginResult getResult() { return result; }
    public User        getUser()   { return user;   }

    public boolean isSuccess()     { return result == LoginResult.SUCCESS; }
    public boolean isOAuthAccount(){ return result == LoginResult.OAUTH_ACCOUNT; }
}