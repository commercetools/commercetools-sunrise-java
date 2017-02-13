package com.commercetools.sunrise.productcatalog.productoverview;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(CategoryFinderBySlug.class)
@FunctionalInterface
public interface CategoryFinder extends Function<String, CompletionStage<Optional<Category>>> {

    @Override
    CompletionStage<Optional<Category>> apply(String identifier);
}
