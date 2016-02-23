package categorytree;

import com.google.inject.Provider;
import inject.SunriseInitializationException;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.client.SphereClient;
import play.Logger;

import javax.inject.Inject;

class CategoryTreeProvider implements Provider<CategoryTreeExtended> {
    private final SphereClient client;

    @Inject
    private CategoryTreeProvider(final SphereClient client) {
        this.client = client;
    }

    @Override
    public CategoryTreeExtended get() {
        try {
            final RefreshableCategoryTree categoryTree = RefreshableCategoryTree.of(client);
            Logger.info("Provide RefreshableCategoryTree with " + categoryTree.getAllAsFlatList().size() + " categories");
            return categoryTree;
        } catch (RuntimeException e) {
            throw new SunriseInitializationException("Could not fetch categories", e);
        }
    }
}
