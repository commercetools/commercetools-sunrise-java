package categorytree;

import com.google.inject.AbstractModule;
import io.sphere.sdk.categories.CategoryTreeExtended;

import javax.inject.Singleton;

public class CategoryTreeProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CategoryTreeExtended.class).toProvider(CategoryTreeProvider.class).in(Singleton.class);
    }
}
