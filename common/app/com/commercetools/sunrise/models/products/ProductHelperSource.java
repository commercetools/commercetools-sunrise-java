package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.models.categories.NewCategoryTree;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class ProductHelperSource {

    private final Provider<CategoryTree> newCategoryTreeProvider;

    @Inject
    ProductHelperSource(@NewCategoryTree final Provider<CategoryTree> newCategoryTreeProvider) {
        this.newCategoryTreeProvider = newCategoryTreeProvider;
    }

    public CharSequence availabilityColorCode(final Long availableQuantity) {
        final String code;
        if (availableQuantity < 4) {
            code = "red";
        } else if (availableQuantity > 10) {
            code = "green";
        } else {
            code = "orange";
        }
        return code;
    }

    public CharSequence ifNew(final ProductProjection product, final Options options) throws IOException {
        final CategoryTree newCategoryTree = newCategoryTreeProvider.get();
        final boolean isNew = product.getCategories().stream()
                .anyMatch(categoryRef -> newCategoryTree.findById(categoryRef.getId()).isPresent());
        return isNew ? options.fn() : null;
    }
}
