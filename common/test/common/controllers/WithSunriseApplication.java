package common.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.HttpRequestIntent;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.F;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static play.inject.Bindings.bind;

public abstract class WithSunriseApplication extends WithApplication {

    @Override
    protected Application provideApplication() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);

        return new GuiceApplicationBuilder()
                .overrides(bind(PlayJavaSphereClient.class).toInstance(injectedClientInstance()))
                .overrides(bind(CategoryTree.class).toInstance(injectedCategoryTree()))
                .loadConfig(new Configuration(additionalSettings).withFallback(Configuration.load(new Environment(Mode.TEST))))
                .build();
    }

    protected CategoryTree injectedCategoryTree() {
        final TypeReference<PagedQueryResult<Category>> typeReference = new TypeReference<PagedQueryResult<Category>>() {};
        final PagedQueryResult<Category> categoryPagedQueryResult = readObjectFromResource("categories.json", typeReference);
        return CategoryTree.of(categoryPagedQueryResult.getResults());
    }

    protected final PlayJavaSphereClient injectedClientInstance() {
        return new PlayJavaSphereClient() {

            @SuppressWarnings("unchecked")
            @Override
            public <T> F.Promise<T> execute(final SphereRequest<T> sphereRequest) {
                final T result = (T) getTestDoubleBehavior().apply(sphereRequest.httpRequestIntent());
                return F.Promise.pure(result);
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
