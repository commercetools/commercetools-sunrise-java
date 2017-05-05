package com.commercetools.sunrise.categorytree;

import com.commercetools.sunrise.test.JsonUtils;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QuerySort;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryTreeProviderTest {

    private static PagedQueryResult<Category> categoryQueryResult = JsonUtils.readCtpObject("categorytree/providerCategoryQueryResult.json", CategoryQuery.resultTypeReference());

    private SphereClient sphereClient;
    private CategoryTreeConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        this.configuration = mock(CategoryTreeConfiguration.class);
        this.sphereClient = mock(SphereClient.class);
        when(sphereClient.execute(any(CategoryQuery.class))).thenReturn(completedFuture(categoryQueryResult));
    }

    @Test
    public void buildsCategoryTreeWithoutSorting() throws Exception {
        when(configuration.sortExpressions()).thenReturn(emptyList());
        assertThat(provideCategoryTree().getAllAsFlatList()).hasSize(131);
        verify(sphereClient).execute(queryAllBaseQuery());
    }

    @Test
    public void buildsCategoryTreeWithSorting() throws Exception {
        when(configuration.sortExpressions()).thenReturn(asList("sort A", "sort B", "sort C"));
        assertThat(provideCategoryTree().getAllAsFlatList()).hasSize(131);
        verify(sphereClient).execute(queryAllBaseQuery()
                .withSort(asList(QuerySort.of("sort A"), QuerySort.of("sort B"), QuerySort.of("sort C"))));
    }

    private CategoryQuery queryAllBaseQuery() {
        return CategoryQuery.of()
                .withSort(model -> model.id().sort().asc())
                .withLimit(500)
                .withOffset(0);
    }

    private CategoryTree provideCategoryTree() {
        return new CategoryTreeProvider(configuration, CompletableFuture::completedFuture, sphereClient).get();
    }
}