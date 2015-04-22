package plugins;

import com.google.inject.Provider;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
class CategoryTreeProvider implements Provider<CategoryTree> {
    private final PlayJavaSphereClient client;

    @Inject
    private CategoryTreeProvider(final PlayJavaSphereClient client) {
        this.client = client;
    }

    @Override
    public CategoryTree get() {
        Logger.info("execute CategoryTreeProvider.get()");
        final PagedQueryResult<Category> pagedQueryResult = client.execute(CategoryQuery.of()).get(3000, TimeUnit.MILLISECONDS);//TODO this will be most likely moved to a plugin
        return CategoryTree.of(pagedQueryResult.getResults());
    }
}
