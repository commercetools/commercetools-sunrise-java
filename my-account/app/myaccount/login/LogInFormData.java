package myaccount.login;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class LogInFormData extends Base {
    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}

