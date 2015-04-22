package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.*;
import io.sphere.sdk.json.JsonUtils;
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

import static play.inject.Bindings.bind;

public abstract class WithSunriseApplication extends WithApplication {
    @Override
    protected Application provideApplication() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);

        Application application = new GuiceApplicationBuilder()
                .overrides(bind(PlayJavaSphereClient.class).toInstance(injectedClientInstance(app)))
                .overrides(bind(CategoryTree.class).toInstance(injectedCategoryTree()))
                .loadConfig(new Configuration(additionalSettings).withFallback(Configuration.load(new Environment(Mode.TEST))))
                .build();
        return application;
    }

    private CategoryTree injectedCategoryTree() {
        final TypeReference<PagedQueryResult<Category>> typeReference = new TypeReference<PagedQueryResult<Category>>() {

        };
        final PagedQueryResult<Category> categoryPagedQueryResult =
                JsonUtils.readObjectFromResource("categories.json", typeReference);
        return CategoryTree.of(categoryPagedQueryResult.getResults());
    }

    protected final PlayJavaSphereClient injectedClientInstance(final Application app) {
        return createObjectTestDoubleFromRequestablePlay(getTestDoubleBehavior());
    }

    protected abstract Function<HttpRequestIntent, Object> getTestDoubleBehavior();

    @SuppressWarnings("unchecked")
    public PlayJavaSphereClient createObjectTestDoubleFromRequestablePlay(final Function<HttpRequestIntent, Object> function) {
        return new PlayJavaSphereClient() {
            @Override
            public <T> F.Promise<T> execute(final SphereRequest<T> sphereRequest) {
                final T result = (T) function.apply(sphereRequest.httpRequestIntent());
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
}
