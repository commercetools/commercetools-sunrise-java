package myaccount;

import io.sphere.sdk.models.Base;

public class UserBean extends Base {

    private boolean loggedIn;
    private String name;

    public UserBean() {
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
