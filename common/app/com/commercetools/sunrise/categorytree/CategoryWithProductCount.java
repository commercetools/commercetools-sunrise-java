package com.commercetools.sunrise.categorytree;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Identifiable;

public final class CategoryWithProductCount extends Base implements Identifiable<Category> {

    private final String id;
    private final boolean hasProducts;

    @JsonCreator
    public CategoryWithProductCount(@JsonProperty("id") final String id, @JsonProperty("productCount") final int productCount) {
        this.id = id;
        this.hasProducts = productCount > 0;
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean hasProducts() {
        return hasProducts;
    }
}
