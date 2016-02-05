package categorytree;

import com.google.inject.AbstractModule;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;

import java.util.List;

public class CategoryTreeTestModule extends AbstractModule {
    private final List<Category> categories;

    public CategoryTreeTestModule(final List<Category> categories) {
        this.categories = categories;
    }

    @Override
    protected void configure() {
        bind(CategoryTreeExtended.class).toInstance(CategoryTreeExtended.of(categories));
    }
}
