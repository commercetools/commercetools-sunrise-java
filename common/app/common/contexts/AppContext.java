package common.contexts;

import common.utils.Translator;

public class AppContext {
    private final UserContext userContext;
    private final ProjectContext projectContext;
    private final Translator translator;

    private AppContext(final UserContext userContext, final ProjectContext projectContext) {
        this.userContext = userContext;
        this.projectContext = projectContext;
        this.translator = Translator.of(userContext.language(), userContext.fallbackLanguages(), projectContext.languages());
    }

    public UserContext user() {
        return userContext;
    }

    public ProjectContext project() {
        return projectContext;
    }

    public Translator translator() {
        return translator;
    }

    public static AppContext of(final UserContext userContext, final ProjectContext projectContext) {
        return new AppContext(userContext, projectContext);
    }
}
