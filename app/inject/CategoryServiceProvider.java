package inject;

import com.google.inject.Provider;
import io.sphere.sdk.categories.CategoryTree;
import productcatalog.services.CategoryService;
import productcatalog.services.CategoryServiceImpl;

import javax.inject.Inject;

public class CategoryServiceProvider implements Provider<CategoryService> {
    private final CategoryTree categories;

    @Inject
    public CategoryServiceProvider(final CategoryTree categories) {
        this.categories = categories;
    }

    @Override
    public CategoryService get() {
        return new CategoryServiceImpl(categories);
    }
}
