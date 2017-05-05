package com.commercetools.sunrise.categorytree;

import com.commercetools.sunrise.it.WithSphereClient;
import io.sphere.sdk.categories.queries.CategoryQueryModel;
import io.sphere.sdk.products.ProductDraft;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryPredicate;
import io.sphere.sdk.queries.QuerySort;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static com.commercetools.sunrise.it.CategoryTestFixtures.categoryDraft;
import static com.commercetools.sunrise.it.CategoryTestFixtures.withCategory;
import static com.commercetools.sunrise.it.ProductTestFixtures.*;
import static com.commercetools.sunrise.it.ProductTypeTestFixtures.productTypeDraft;
import static com.commercetools.sunrise.it.ProductTypeTestFixtures.withProductType;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoriesWithProductCountQueryIntegrationTest extends WithSphereClient {

    @Test
    public void isExecuted() throws Exception {
        withProductAssignedToOneCategory(categoryIds -> {
            final CategoriesWithProductCountQuery query = CategoriesWithProductCountQuery.of()
                    .withPredicates(QueryPredicate.of(CategoryQueryModel.of().id().isIn(categoryIds).toSphereQuery()))
                    .withSort(QuerySort.of(CategoryQueryModel.of().createdAt().sort().asc().toSphereSort()))
                    .withLimit(2)
                    .withOffset(1);
            final PagedQueryResult<CategoryWithProductCount> result = sphereClient.executeBlocking(query);
            assertThat(result.getOffset()).as("offset").isEqualTo(1);
            assertThat(result.getCount()).as("count").isEqualTo(2);
            assertThat(result.getTotal()).as("total").isEqualTo(3);
            assertThat(result.getResults())
                    .as("results")
                    .extracting(CategoryWithProductCount::hasProducts)
                    .isNotNull();
        });
    }

    private void withProductAssignedToOneCategory(final Consumer<List<String>> test) {
        withCategory(sphereClient, categoryDraft(), category1 -> {
            withCategory(sphereClient, categoryDraft(), category2 -> {
                withCategory(sphereClient, categoryDraft(), category3 -> {
                    withProductType(sphereClient, productTypeDraft(), productType -> {
                        final ProductDraft productDraft = productDraft(productType, productVariantDraft())
                                .categories(asList(category1.toReference(), category2.toReference()))
                                .publish(true)
                                .build();
                        withProduct(sphereClient, productDraft, product -> {
                            test.accept(asList(category1.getId(), category2.getId(), category3.getId()));
                            return product;
                        });
                        return productType;
                    });
                    return category3;
                });
                return category2;
            });
            return category1;
        });
    }
}
