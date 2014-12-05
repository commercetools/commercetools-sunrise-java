package plugins;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.typesafe.config.ConfigFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.client.PlayJavaClientImpl;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Application;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which
 * shall be used in production and integration tests.
 */
class ProductionModule extends AbstractModule {
    private final Application app;

    public ProductionModule(Application app) {
        this.app = app;
    }

    @Override
    protected void configure() {
        bind(PlayJavaClient.class).toProvider(PlayJavaClientProvider.class).in(Singleton.class);
        bind(CategoryTree.class).toProvider(CategoryTreeProvider.class).in(Singleton.class);
        bind(Configuration.class).toInstance(app.configuration());
    }

    @Singleton
    private static class CategoryTreeProvider implements Provider<CategoryTree> {
        private final PlayJavaClient client;

        @Inject
        private CategoryTreeProvider(final PlayJavaClient client) {
            this.client = client;
        }

        @Override
        public CategoryTree get() {
            Logger.info("categoryTreeProvider");
            final PagedQueryResult<Category> pagedQueryResult = client.execute(CategoryQuery.of()).get(2000, TimeUnit.MILLISECONDS);//TODO this will be most likely moved to a plugin
            return CategoryTree.of(pagedQueryResult.getResults());
        }
    }


    @Singleton
    private static class PlayJavaClientProvider implements Provider<PlayJavaClient> {
        @Override
        public PlayJavaClient get() {
            Logger.info("playJavaClientProvider");
            return new PlayJavaClientImpl(ConfigFactory.load());
        }
    }
}
