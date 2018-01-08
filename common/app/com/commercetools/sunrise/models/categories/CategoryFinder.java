package com.commercetools.sunrise.models.categories;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;

import java.util.Optional;
import java.util.function.Function;

@ImplementedBy(CategoryFinderImpl.class)
@FunctionalInterface
// TODO transform to in cache, same with category tree
public interface CategoryFinder extends Function<String, Optional<Category>> {

    @Override
    Optional<Category> apply(String identifier);
}
