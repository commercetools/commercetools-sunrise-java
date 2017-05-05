package com.commercetools.sunrise.categorytree;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.json.SphereJsonUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryWithProductCountTest {

    @Test
    public void readsCategoryWithProducts() throws Exception {
        final JsonNode categoryWithProducts = provideCategoryResponse(355);
        final CategoryWithProductCount object = SphereJsonUtils.readObject(categoryWithProducts, CategoryWithProductCount.class);
        assertThat(object.getId()).isEqualTo("9d7b89e7-dca4-47e3-b97c-d71a7a104391");
        assertThat(object.hasProducts()).isTrue();
    }

    @Test
    public void readsCategoryWithoutProducts() throws Exception {
        final JsonNode categoryWithoutProducts = provideCategoryResponse(0);
        final CategoryWithProductCount object = SphereJsonUtils.readObject(categoryWithoutProducts, CategoryWithProductCount.class);
        assertThat(object.hasProducts()).isFalse();
    }

    @Test
    public void readsCategoryWithOneProduct() throws Exception {
        final JsonNode categoryWithoutProducts = provideCategoryResponse(1);
        final CategoryWithProductCount object = SphereJsonUtils.readObject(categoryWithoutProducts, CategoryWithProductCount.class);
        assertThat(object.hasProducts()).isTrue();
    }

    private JsonNode provideCategoryResponse(final int productCount) {
        return SphereJsonUtils.parse("{\n" +
                "\"id\": \"9d7b89e7-dca4-47e3-b97c-d71a7a104391\",\n" +
                "\"productCount\": " + productCount+ "\n" +
                "}");
    }
}
