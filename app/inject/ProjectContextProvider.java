package inject;

import com.google.inject.Provider;
import common.contexts.ProjectContext;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

@Singleton
class ProjectContextProvider implements Provider<ProjectContext> {
    private final SphereClient client;

    @Inject
    private ProjectContextProvider(final SphereClient client) {
        this.client = client;
    }

    @Override
    public ProjectContext get() {
        try {
            final Project project = client.execute(ProjectGet.of()).toCompletableFuture().get();
            Logger.debug("Provide ProjectContext:"
                    + " Languages" + project.getLanguages() + ","
                    + " Countries" + project.getCountries());
            final List<Locale> languages = project.getLanguages().stream().map(Locale::forLanguageTag).collect(toList());
            return ProjectContext.of(languages, project.getCountries());
        } catch (ExecutionException | InterruptedException e) {
            throw new SunriseInitializationException("Could not fetch project information", e);
        }
    }
}
