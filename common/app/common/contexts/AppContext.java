package common.contexts;

public class AppContext {
    private final UserContext userContext;
    private final ProjectContext projectContext;

    private AppContext(final UserContext userContext, final ProjectContext projectContext) {
        this.userContext = userContext;
        this.projectContext = projectContext;
    }

    public UserContext user() {
        return userContext;
    }

    public ProjectContext project() {
        return projectContext;
    }

    public static AppContext of(final UserContext userContext, final ProjectContext projectContext) {
        return new AppContext(userContext, projectContext);
    }
}
