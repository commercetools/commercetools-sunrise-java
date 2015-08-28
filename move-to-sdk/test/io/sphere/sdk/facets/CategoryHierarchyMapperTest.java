package io.sphere.sdk.facets;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.json.SphereJsonUtils;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryHierarchyMapperTest {
    private static final List<Locale> LOCALES = singletonList(Locale.ENGLISH);
    private final static String A_ID = "d5a0952b-6574-49c9-b0cd-61e0d21d36cc";
    private final static String B_ID = "e92b6d26-7a34-4960-804c-0fc9e40c64e3";
    private final static String C_ID = "1acce167-cd23-4fd7-b344-af76941cb375";
    private final static String D_ID = "f5b288e8-d19c-4c0d-a18a-8ca68f982b8e";
    private final static String E_ID = "b00b1eb9-f051-4f13-8f9c-1bb73e13e8a1";
    private final static String F_ID = "57723822-c57f-4f3b-b136-8256ed75390c";
    private static final Category A = category(A_ID, null, "A");
    private static final Category B = category(B_ID, A_ID, "B");
    private static final Category C = category(C_ID, B_ID, "C");
    private static final Category D = category(D_ID, C_ID, "D");
    private static final Category E = category(E_ID, C_ID, "E");
    private static final Category F = category(F_ID, A_ID, "F");
    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(asList(A, B, C, D, E, F));

    /* A
     * |-- B
     * |   |-- C
     * |       |-- D
     * |       |-- E
     * |-- F
     */

    @Test
    public void withLeafCategory() throws Exception {
        List<FacetOption> facetOptions = singletonList(FacetOption.of(E_ID, 3, true));
        final CategoryTree categoryTree = CATEGORY_TREE;
        final List<Category> rootCategories = singletonList(E);
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, LOCALES).build();
        assertThat(hierarchicalOptions).containsExactly(FacetOption.of("E", 3, true));
    }

    @Test
    public void withMissingLocale() throws Exception {
        List<FacetOption> facetOptions = singletonList(FacetOption.of(E_ID, 3, true));
        final CategoryTree categoryTree = CATEGORY_TREE;
        final List<Category> rootCategories = singletonList(E);
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, singletonList(GERMAN)).build();
        assertThat(hierarchicalOptions).isEmpty();
    }

    @Test
    public void withEmptyFacetOptions() throws Exception {
        List<FacetOption> facetOptions = emptyList();
        final CategoryTree categoryTree = CATEGORY_TREE;
        final List<Category> rootCategories = singletonList(E);
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, LOCALES).build();
        assertThat(hierarchicalOptions).isEmpty();
    }

    @Test
    public void withEmptyCategoryTree() throws Exception {
        List<FacetOption> facetOptions = asList(FacetOption.of(E_ID, 3, true), FacetOption.of(B_ID, 2, false));
        final CategoryTree categoryTree = CategoryTree.of(emptyList());
        final List<Category> rootCategories = asList(B, C, D, E);
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, LOCALES).build();
        assertThat(hierarchicalOptions).containsExactly(FacetOption.of("B", 2, false), FacetOption.of("E", 3, true));
    }

    @Test
    public void withEmptyRootCategories() throws Exception {
        List<FacetOption> facetOptions = asList(FacetOption.of(E_ID, 3, true), FacetOption.of(B_ID, 2, false));
        final CategoryTree categoryTree = CATEGORY_TREE;
        final List<Category> rootCategories = emptyList();
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, LOCALES).build();
        assertThat(hierarchicalOptions).isEmpty();
    }

    @Test
    public void withTree() throws Exception {
        List<FacetOption> facetOptions = asList(FacetOption.of(E_ID, 3, true), FacetOption.of(D_ID, 2, false));
        final CategoryTree categoryTree = CATEGORY_TREE;
        final List<Category> rootCategories = singletonList(C);
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, LOCALES).build();
        final FacetOption expectedOptions = FacetOption.of("C", 5, true)
                .withChildren(asList(FacetOption.of("D", 2, false), FacetOption.of("E", 3, true)));
        assertThat(hierarchicalOptions).containsExactly(expectedOptions);
    }

    @Test
    public void withEmptyBranches() throws Exception {
        List<FacetOption> facetOptions = asList(FacetOption.of(E_ID, 3, true), FacetOption.of(D_ID, 2, false), FacetOption.of(B_ID, 1, false));
        final CategoryTree categoryTree = CATEGORY_TREE;
        final List<Category> rootCategories = singletonList(A);
        final List<FacetOption> hierarchicalOptions = CategoryHierarchyMapper.of(facetOptions, categoryTree, rootCategories, LOCALES).build();
        final FacetOption expectedOptions = FacetOption.of("A", 6, true)
                .withChildren(singletonList(FacetOption.of("B", 6, true)
                        .withChildren(singletonList(FacetOption.of("C", 5, true)
                                .withChildren(asList(FacetOption.of("D", 2, false), FacetOption.of("E", 3, true)))))));
        assertThat(hierarchicalOptions).containsExactly(expectedOptions);
    }

    private static Category category(final String id, @Nullable final String parentId, final String name) {
        final String parentJson = Optional.ofNullable(parentId).map(p ->
                ", \"parent\": {" +
                "    \"typeId\": \"category\"," +
                "    \"id\": \""+ p +"\"" +
                "  }").orElse("");
        final String json =
             "{" +
             "  \"id\": \"" + id + "\"," +
             "  \"version\": 1," +
             "  \"name\": {" +
             "    \"en\": \"" + name + "\"" +
             "  }," +
             "  \"slug\": {" +
             "    \"en\": \"" + name + "-slug\"" +
             "  }" +
                parentJson +
             "}";
        return SphereJsonUtils.readObject(json, Category.typeReference());
    }
}
