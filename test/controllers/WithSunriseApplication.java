package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.*;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.Requestable;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.utils.JsonUtils;
import play.Application;
import play.Configuration;
import play.libs.F;
import plugins.Global;
import play.test.FakeApplication;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static play.test.Helpers.fakeApplication;

public abstract class WithSunriseApplication extends WithApplication {
    @Override
    protected FakeApplication provideFakeApplication() {
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.settingsWidget.enabled", false);
        return fakeApplication(additionalSettings, new Global() {
            @Override
            protected Injector createInjector(final Application app) {
                return Guice.createInjector(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PlayJavaSphereClient.class).toInstance(injectedClientInstance(app));
                        bind(CategoryTree.class).toInstance(injectedCategoryTree());
                        bind(Configuration.class).toInstance(app.configuration());
                    }
                });
            }
        });
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

    protected abstract Function<Requestable, Object> getTestDoubleBehavior();

    /**
     * Override this to add additional settings
     * @param app the application used
     * @return a configuration containing the {@code app} configuration values and overridden values
     */
    protected Configuration getConfiguration(Application app) {
        return app.configuration();
    }

    public PlayJavaSphereClient createObjectTestDoubleFromRequestablePlay(final Function<Requestable, Object> function) {
        return new PlayJavaSphereClient() {
            @Override
            public <T> F.Promise<T> execute(final SphereRequest<T> sphereRequest) {
                final T result = (T) function.apply(sphereRequest);
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
