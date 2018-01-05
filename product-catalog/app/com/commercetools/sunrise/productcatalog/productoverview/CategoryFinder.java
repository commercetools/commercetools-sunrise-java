package com.commercetools.sunrise.productcatalog.productoverview;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;

import java.util.Optional;
import java.util.function.Function;

@ImplementedBy(CategoryFinderImpl.class)
@FunctionalInterface
public interface CategoryFinder extends Function<String, Optional<Category>> {

    @Override
    Optional<Category> apply(String identifier);
}
