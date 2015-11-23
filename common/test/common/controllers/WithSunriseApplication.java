package common.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import io.sphere.sdk.queries.PagedQueryResult;
import org.apache.commons.io.IOUtils;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static io.sphere.sdk.json.SphereJsonUtils.readObject;
import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static play.inject.Bindings.bind;

public abstract class WithSunriseApplication extends WithApplication {

    @Override
    protected Application provideApplication() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);

        return new GuiceApplicationBuilder()
                .overrides(bind(SphereClient.class).toInstance(injectedClientInstance()))
                .overrides(bind(CategoryTree.class).toInstance(injectedCategoryTree()))
                .loadConfig(new Configuration(additionalSettings).withFallback(Configuration.load(new Environment(Mode.TEST))))
                .build();
    }

    protected CategoryTree injectedCategoryTree() {
        final TypeReference<PagedQueryResult<Category>> typeReference = new TypeReference<PagedQueryResult<Category>>() {};
        final PagedQueryResult<Category> categoryPagedQueryResult = readObjectFromResource("controllers/categories.json", typeReference);
        return CategoryTree.of(categoryPagedQueryResult.getResults());
    }

    private static String stringFromResource(final String resourcePath) {
        try {
            return IOUtils.toString(WithSunriseApplication.class.getResourceAsStream(resourcePath), "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    protected final SphereClient injectedClientInstance() {
        return new SphereClient() {

            @SuppressWarnings("unchecked")
            @Override
            public <T> CompletionStage<T> execute(final SphereRequest<T> sphereRequest) {
                final T result = (T) getResult(sphereRequest);
                return CompletableFuture.completedFuture(result);
            }

            private <T> Object getResult(final SphereRequest<T> sphereRequest) {
                if (sphereRequest.httpRequestIntent().equals(ProjectGet.of().httpRequestIntent())) {
                    final String projectJsonString = stringFromResource("/ctp/project/project.json");
                    return readObject(projectJsonString, Project.typeReference());
                } else {
                    return getTestDoubleBehavior().apply(sphereRequest.httpRequestIntent());
                }
            }

            @Override
            public void close() {
            }

            @Override
            public String toString() {
                return "SphereClientObjectTestDouble";
            }
        };
    }

    protected abstract Function<HttpRequestIntent, Object> getTestDoubleBehavior();
}
