package inject;

import com.google.inject.Provider;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;
import productcatalog.services.CategoryService;
import productcatalog.services.CategoryServiceImpl;

import javax.inject.Inject;

public class CategoryServiceProvider implements Provider<CategoryService> {
    private final CategoryTree categories;
    private final Configuration configuration;

    @Inject
    public CategoryServiceProvider(final CategoryTree categories, final Configuration configuration) {
        this.categories = categories;
        this.configuration = configuration;
    }

    @Override
    public CategoryService get() {
        return new CategoryServiceImpl(categories, configuration);
    }
}
