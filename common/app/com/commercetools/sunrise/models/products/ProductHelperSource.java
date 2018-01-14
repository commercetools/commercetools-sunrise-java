package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.models.categories.NewCategoryTree;
import com.commercetools.sunrise.models.prices.Price;
import com.commercetools.sunrise.models.prices.PriceFactory;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Optional;

@Singleton
public class ProductHelperSource {

    private final PriceFactory priceFactory;
    private final Provider<CategoryTree> newCategoryTreeProvider;

    @Inject
    ProductHelperSource(final PriceFactory priceFactory,
                        @NewCategoryTree Provider<CategoryTree> newCategoryTreeProvider) {
        this.priceFactory = priceFactory;
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

    public CharSequence withProductPrice(final ProductVariant variant, final Options options) throws IOException {
        final Optional<Price> price = priceFactory.create(variant);
        return price.isPresent() ? options.fn(price.get()) : null;
    }

    public CharSequence ifDiscounted(final ProductVariant variant, final Options options) throws IOException {
        final boolean discounted = ProductPriceUtils.hasDiscount(variant);
        return discounted ? options.fn() : null;
    }

    public CharSequence ifNew(final ProductProjection product, final Options options) throws IOException {
        final CategoryTree newCategoryTree = newCategoryTreeProvider.get();
        final boolean isNew = product.getCategories().stream()
                .anyMatch(categoryRef -> newCategoryTree.findById(categoryRef.getId()).isPresent());
        return isNew ? options.fn() : null;
    }
}
