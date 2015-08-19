package common.contexts;

public class AppContext {
    private final ProjectContext projectContext;

    private AppContext(final ProjectContext projectContext) {
        this.projectContext = projectContext;

    }

    public ProjectContext project() {
        return projectContext;
    }

    public static AppContext of(final ProjectContext projectContext) {
        return new AppContext(projectContext);
    }
}
