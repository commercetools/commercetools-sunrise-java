package plugins;

import com.google.inject.AbstractModule;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.client.PlayJavaClientImpl;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Application;
import play.Configuration;

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
        final PlayJavaClient client = new PlayJavaClientImpl(app.configuration());
        final PagedQueryResult<Category> queryResult = client.execute(CategoryQuery.of()).get(2000, TimeUnit.MILLISECONDS);//TODO this will be most likely moved to a plugin
        //TODO this does not take all categories if there are a lot.
        final CategoryTree categoryTree = CategoryTree.of(queryResult.getResults());
        bind(PlayJavaClient.class).toInstance(client);
        bind(CategoryTree.class).toInstance(categoryTree);
        bind(Configuration.class).toInstance(app.configuration());
    }
}
