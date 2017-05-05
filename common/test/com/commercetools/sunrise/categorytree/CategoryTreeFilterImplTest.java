package com.commercetools.sunrise.categorytree;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static com.commercetools.sunrise.test.JsonUtils.readCtpObject;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryTreeFilterImplTest {

    private static List<Category> categoryList = readCtpObject("categorytree/filterCategoryList.json", new TypeReference<List<Category>>() {});
    private static PagedQueryResult<CategoryWithProductCount> categoriesWithProductCountQueryResult = readCtpObject("categorytree/categoriesWithProductCountQueryResult.json", CategoriesWithProductCountQuery.resultTypeReference());

    private SphereClient sphereClient;
    private CategoryTreeConfiguration configuration;
    private CategoryTree categoryTree;

    @Before
    public void setUp() throws Exception {
        this.configuration = mock(CategoryTreeConfiguration.class);
        this.sphereClient = mock(SphereClient.class);
        when(sphereClient.execute(any(CategoriesWithProductCountQuery.class))).thenReturn(completedFuture(categoriesWithProductCountQueryResult));
        this.categoryTree = CategoryTree.of(categoryList);
    }

    @Test
    public void keepsEmptyCategories() throws Exception {
        when(configuration.discardEmpty()).thenReturn(false);
        final CategoryTree filteredCategoryTree = filterCategoryTree(categoryTree);
        assertThat(filteredCategoryTree).isSameAs(categoryTree);
        assertThat(filteredCategoryTree.getAllAsFlatList()).hasSize(8);
    }

    @Test
    public void discardsEmptyCategories() throws Exception {
        when(configuration.discardEmpty()).thenReturn(true);
        final CategoryTree filteredCategoryTree = filterCategoryTree(categoryTree);
        assertThat(filteredCategoryTree).isNotSameAs(categoryTree);
        assertThat(filteredCategoryTree.getAllAsFlatList())
                .hasSize(3)
                .extracting(category -> category.getSlug().get(Locale.GERMAN))
                .containsOnly("A", "A-2", "A-2-1");
    }

    private CategoryTree filterCategoryTree(final CategoryTree categoryTree) {
        return new CategoryTreeFilterImpl(configuration, sphereClient).filter(categoryTree).toCompletableFuture().join();
    }
}
