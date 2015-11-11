package io.sphere.sdk.facets;

import io.sphere.sdk.categories.Category;
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

public class HierarchicalCategoryFacetOptionMapperTest {
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

    /* A
     * |-- B
     * |   |-- C
     * |   |-- D
     * |
     * |-- E
     */

    @Test
    public void replacesIdForName() throws Exception {
        final List<Category> subcategories = singletonList(CAT_D);
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, LOCALES)
                .apply(singletonList(OPTION_D));
        assertThat(hierarchicalOptions).containsExactly(OPTION_D.withLabel("D"));
    }

    @Test
    public void keepsOrderFromCategoryTree() throws Exception {
        final List<Category> subcategories = asList(CAT_C, CAT_D);
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, LOCALES)
                .apply(asList(OPTION_D, OPTION_C));
        assertThat(hierarchicalOptions).containsExactly(OPTION_C.withLabel("C"), OPTION_D.withLabel("D"));
    }

    @Test
    public void discardsOnMissingLocale() throws Exception {
        final List<Category> subcategories = singletonList(CAT_D);
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, singletonList(GERMAN))
                .apply(singletonList(OPTION_D));
        assertThat(hierarchicalOptions).isEmpty();
    }

    @Test
    public void discardsWithEmptyCategoryTree() throws Exception {
        final List<Category> subcategories = emptyList();
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, LOCALES)
                .apply(asList(OPTION_D, OPTION_C));
        assertThat(hierarchicalOptions).isEmpty();
    }

    @Test
    public void worksWithEmptyFacetOptions() throws Exception {
        final List<Category> subcategories = singletonList(CAT_D);
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, LOCALES)
                .apply(emptyList());
        assertThat(hierarchicalOptions).isEmpty();
    }

    @Test
    public void inheritsInformationFromLeaves() throws Exception {
        final List<Category> subcategories = asList(CAT_B, CAT_C, CAT_D);
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, LOCALES)
                .apply(asList(OPTION_D, OPTION_C, OPTION_B));
        final FacetOption expectedOptions = OPTION_B
                .withLabel("B")
                .withCount(sumCount(OPTION_B, OPTION_C, OPTION_D))
                .withSelected(isAnySelected(OPTION_B, OPTION_C, OPTION_D))
                .withChildren(asList(OPTION_C.withLabel("C"), OPTION_D.withLabel("D")));
        assertThat(hierarchicalOptions).containsExactly(expectedOptions);
    }

    @Test
    public void discardsEmptyBranches() throws Exception {
        final List<Category> subcategories = asList(CAT_A, CAT_B, CAT_C, CAT_D, CAT_E);
        final List<FacetOption> hierarchicalOptions = HierarchicalCategoryFacetOptionMapper.of(subcategories, LOCALES)
                .apply(asList(OPTION_D, OPTION_C, OPTION_B));
        final FacetOption expectedOptions = OPTION_A
                .withLabel("A")
                .withCount(sumCount(OPTION_A, OPTION_B, OPTION_C, OPTION_D))
                .withSelected(isAnySelected(OPTION_A, OPTION_B, OPTION_C, OPTION_D))
                .withChildren(singletonList(OPTION_B
                        .withLabel("B")
                        .withCount(sumCount(OPTION_B, OPTION_C, OPTION_D))
                        .withSelected(isAnySelected(OPTION_B, OPTION_C, OPTION_D))
                        .withChildren(asList(OPTION_C.withLabel("C"), OPTION_D.withLabel("D")))));
        assertThat(hierarchicalOptions).containsExactly(expectedOptions);
    }

    private long sumCount(final FacetOption... facetOptions) {
        return asList(facetOptions).stream().mapToLong(FacetOption::getCount).sum();
    }

    private boolean isAnySelected(final FacetOption... facetOptions) {
        return asList(facetOptions).stream().anyMatch(FacetOption::isSelected);
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
