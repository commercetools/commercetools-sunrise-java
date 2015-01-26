package plugins;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.PlayJavaSphereClientFactory;
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
public class ProductionModule extends AbstractModule {
    private final Application app;

    public ProductionModule(Application app) {
        this.app = app;
    }

    @Override
    protected void configure() {
        bind(PlayJavaSphereClient.class).toProvider(PlayJavaSphereClientProvider.class).in(Singleton.class);
        bind(CategoryTree.class).toProvider(CategoryTreeProvider.class).in(Singleton.class);
        bind(Configuration.class).toInstance(app.configuration());
    }

    @Singleton
    private static class CategoryTreeProvider implements Provider<CategoryTree> {
        private final PlayJavaSphereClient client;

        @Inject
        private CategoryTreeProvider(final PlayJavaSphereClient client) {
            this.client = client;
        }

        @Override
        public CategoryTree get() {
            Logger.info("execute CategoryTreeProvider.get()");
            final PagedQueryResult<Category> pagedQueryResult = client.execute(CategoryQuery.of()).get(2000, TimeUnit.MILLISECONDS);//TODO this will be most likely moved to a plugin
            return CategoryTree.of(pagedQueryResult.getResults());
        }
    }


    @Singleton
    private static class PlayJavaSphereClientProvider implements Provider<PlayJavaSphereClient> {
        @Override
        public PlayJavaSphereClient get() {
            return createClient();
        }
    }

    public static PlayJavaSphereClient createClient() {
        Logger.info("execute PlayJavaSphereClientProvider.get()");
        final Config classpathConf = ConfigFactory.load();
        final String project = classpathConf.getString("sphere.project");
        final String clientId = classpathConf.getString("sphere.clientId");
        final String clientSecret = classpathConf.getString("sphere.clientSecret");
        return PlayJavaSphereClientFactory.of().createClient(project, clientId, clientSecret);
    }
}
