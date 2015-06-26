package inject;

import com.google.inject.Provider;
import common.contexts.ProjectContext;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Singleton
class ProjectContextProvider implements Provider<ProjectContext> {
    private final PlayJavaSphereClient client;

    @Inject
    private ProjectContextProvider(final PlayJavaSphereClient client) {
        this.client = client;
    }

    @Override
    public ProjectContext get() {
        Logger.debug("Provide ProjectContext");
        final Project project = client.execute(ProjectGet.of()).get(3000, TimeUnit.MILLISECONDS);
        final List<Locale> languages = project.getLanguages().stream().map(Locale::forLanguageTag).collect(toList());
        return new ProjectContext(languages, project.getCountries());
    }
}
