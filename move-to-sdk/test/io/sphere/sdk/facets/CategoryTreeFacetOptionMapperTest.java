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

public class CategoryTreeFacetOptionMapperTest {
    private static final List<Locale> LOCALES = singletonList(Locale.ENGLISH);
    private final static String CAT_A_ID = "d5a0952b-6574-49c9-b0cd-61e0d21d36cc";
    private final static String CAT_B_ID = "e92b6d26-7a34-4960-804c-0fc9e40c64e3";
    private final static String CAT_C_ID = "1acce167-cd23-4fd7-b344-af76941cb375";
    private final static String CAT_D_ID = "f5b288e8-d19c-4c0d-a18a-8ca68f982b8e";
    private final static String CAT_E_ID = "b00b1eb9-f051-4f13-8f9c-1bb73e13e8a1";
    private static final Category CAT_A = category(CAT_A_ID, null, "A");
    private static final Category CAT_B = category(CAT_B_ID, CAT_A_ID, "B");
    private static final Category CAT_C = category(CAT_C_ID, CAT_B_ID, "C");
    private static final Category CAT_D = category(CAT_D_ID, CAT_B_ID, "D");
    private static final Category CAT_E = category(CAT_E_ID, CAT_A_ID, "E");
    private static final FacetOption OPTION_A = FacetOption.of(CAT_A_ID, 0, false);
    private static final FacetOption OPTION_B = FacetOption.of(CAT_B_ID, 1, false);
    private static final FacetOption OPTION_C = FacetOption.of(CAT_C_ID, 2, true);
    private static final FacetOption OPTION_D = FacetOption.of(CAT_D_ID, 3, false);
    private static final List<Category> SELECTED_CATEGORIES = singletonList(CAT_B);

    /* A
     * |-- B
     * |   |-- C
     * |   |-- D
     * |
     * |-- E
     */

    @Test
    public void replacesIdForName() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(singletonList(CAT_D));
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, LOCALES);
        assertThat(mapper.apply(singletonList(OPTION_D)))
                .containsExactly(OPTION_D.withLabel("D").withValue("D-slug"));
    }

    @Test
    public void emptyWhenNotConfigured() throws Exception {
        final CategoryTreeFacetOptionMapper emptyMapper = CategoryTreeFacetOptionMapper.ofEmptyTree();
        assertThat(emptyMapper.apply(singletonList(OPTION_D))).isEmpty();
        final CategoryTree categoryTree = CategoryTree.of(singletonList(CAT_D));
        final CategoryTreeFacetOptionMapper configuredMapper = emptyMapper.withCategories(SELECTED_CATEGORIES, categoryTree, LOCALES);
        assertThat(configuredMapper.apply(singletonList(OPTION_D)))
                .containsExactly(OPTION_D.withLabel("D").withValue("D-slug"));
    }

    @Test
    public void keepsOrderFromCategoryTree() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(asList(CAT_C, CAT_D));
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, LOCALES);
        assertThat(mapper.apply(asList(OPTION_D, OPTION_C)))
                .containsExactly(OPTION_C.withLabel("C").withValue("C-slug"), OPTION_D.withLabel("D").withValue("D-slug"));
    }

    @Test
    public void discardsOnMissingLocale() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(singletonList(CAT_D));
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, singletonList(GERMAN));
        assertThat(mapper.apply(singletonList(OPTION_D))).isEmpty();
    }

    @Test
    public void discardsWithEmptyCategoryTree() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(emptyList());
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, LOCALES);
        assertThat(mapper.apply(asList(OPTION_D, OPTION_C))).isEmpty();
    }

    @Test
    public void worksWithEmptyFacetOptions() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(singletonList(CAT_D));
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, LOCALES);
        assertThat(mapper.apply(emptyList())).isEmpty();
    }

    @Test
    public void inheritsInformationFromLeaves() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(asList(CAT_B, CAT_C, CAT_D));
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, LOCALES);
        final FacetOption expectedOptions = OPTION_B
                .withLabel("B")
                .withValue("B-slug")
                .withCount(sumCount(OPTION_B, OPTION_C, OPTION_D))
                .withSelected(isSelected(OPTION_B))
                .withChildren(asList(OPTION_C.withLabel("C").withValue("C-slug"), OPTION_D.withLabel("D").withValue("D-slug")));
        assertThat(mapper.apply(asList(OPTION_D, OPTION_C, OPTION_B))).containsExactly(expectedOptions);
    }

    @Test
    public void discardsEmptyBranches() throws Exception {
        final CategoryTree categoryTree = CategoryTree.of(asList(CAT_A, CAT_B, CAT_C, CAT_D, CAT_E));
        final CategoryTreeFacetOptionMapper mapper = CategoryTreeFacetOptionMapper.of(SELECTED_CATEGORIES, categoryTree, LOCALES);
        final FacetOption expectedOptions = OPTION_A
                .withLabel("A")
                .withValue("A-slug")
                .withCount(sumCount(OPTION_A, OPTION_B, OPTION_C, OPTION_D))
                .withSelected(isSelected(OPTION_A))
                .withChildren(singletonList(OPTION_B
                        .withLabel("B")
                        .withValue("B-slug")
                        .withCount(sumCount(OPTION_B, OPTION_C, OPTION_D))
                        .withSelected(isSelected(OPTION_B))
                        .withChildren(asList(OPTION_C.withLabel("C").withValue("C-slug"), OPTION_D.withLabel("D").withValue("D-slug")))));
        assertThat(mapper.apply(asList(OPTION_D, OPTION_C, OPTION_B))).containsExactly(expectedOptions);
    }

    private long sumCount(final FacetOption... facetOptions) {
        return asList(facetOptions).stream().mapToLong(FacetOption::getCount).sum();
    }

    private boolean isSelected(final FacetOption facetOptions) {
        return facetOptions.isSelected() || SELECTED_CATEGORIES.stream().anyMatch(c -> c.getId().equals(facetOptions.getValue()));
    }

    private static Category category(final String id, @Nullable final String parentId, final String name) {
        final String parentJson = Optional.ofNullable(parentId).map(p ->
                " \"parent\": {" +
                "    \"typeId\": \"category\"," +
                "    \"id\": \""+ p +"\"" +
                "  },").orElse("");
        final String json =
             "{" +
             "  \"id\": \"" + id + "\"," +
             "  \"version\": 1," +
                 parentJson +
             "  \"name\": {" +
             "    \"en\": \"" + name + "\"" +
             "  }," +
             "  \"slug\": {" +
             "    \"en\": \"" + name + "-slug\"" +
             "  }" +
             "}";
        return SphereJsonUtils.readObject(json, Category.typeReference());
    }
}
