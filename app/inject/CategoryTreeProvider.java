package inject;

import com.google.inject.Provider;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.QueryAll;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Singleton
class CategoryTreeProvider implements Provider<CategoryTree> {
    private final SphereClient client;

    @Inject
    private CategoryTreeProvider(final SphereClient client) {
        this.client = client;
    }

    @Override
    public CategoryTree get() {
        try {
            final QueryAll<Category> query = QueryAll.of(CategoryQuery.of());
            final List<Category> categories = query.run(client).toCompletableFuture().get();
            Logger.debug("Provide CategoryTree with " + categories.size() + " categories");
            return CategoryTree.of(categories);
        } catch (InterruptedException | ExecutionException e) {
            throw new SunriseInitializationException("Could not fetch categories", e);
        }
    }
}
