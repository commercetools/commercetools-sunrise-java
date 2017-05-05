package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.viewmodels;

import com.commercetools.sunrise.framework.localization.UserLanguage;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.forms.FormSelectableOptionViewModel;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import com.commercetools.sunrise.test.TestableCall;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.test.WithApplication;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.fakeRequest;

public class CategoryTreeFacetOptionViewModelFactoryTest extends WithApplication {

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
    private static final TermStats TERM_B = TermStats.of(CAT_B_ID, 4L, 1L);
    private static final TermStats TERM_C = TermStats.of(CAT_C_ID, 8L, 2L);
    private static final TermStats TERM_D = TermStats.of(CAT_D_ID, 7L, 3L);
    private static final CategoryTree ALL_CATEGORY_TREE = CategoryTree.of(asList(CAT_A, CAT_B, CAT_C, CAT_D, CAT_E));
    private static final CategoryTree EMPTY_CATEGORY_TREE = CategoryTree.of(emptyList());

    /* A
     * |-- B
     * |   |-- C
     * |   |-- D
     * |
     * |-- E
     */

    @Override
    protected Application provideApplication() {
        final Application application = super.provideApplication();
        Http.Context.current.set(new Http.Context(fakeRequest()));
        return application;
    }

    @Test
    public void resolvesCategory() throws Exception {
        test(CAT_B, ALL_CATEGORY_TREE, singletonList(TERM_B), viewModel -> {
            assertThat(viewModel.getLabel()).isEqualTo("B");
            assertThat(viewModel.getValue()).isEqualTo("B-slug");
            assertThat(viewModel.getCount()).isEqualTo(1);
            assertThat(viewModel.isSelected()).isFalse();
            assertThat(viewModel.getChildren()).isNullOrEmpty();
        });
    }

    @Test
    public void emptyChildrenWithEmptyCategoryTree() throws Exception {
        test(CAT_B, EMPTY_CATEGORY_TREE, singletonList(TERM_B), viewModel -> {
            assertThat(viewModel.getLabel()).isEqualTo("B");
            assertThat(viewModel.getCount()).isEqualTo(1);
            assertThat(viewModel.isSelected()).isFalse();
            assertThat(viewModel.getChildren()).isNullOrEmpty();
        });
    }

    @Test
    public void zeroCountWithEmptyFacetResults() throws Exception {
        test(CAT_B, ALL_CATEGORY_TREE, emptyList(), viewModel -> {
            assertThat(viewModel.getLabel()).isEqualTo("B");
            assertThat(viewModel.getCount()).isZero();
            assertThat(viewModel.isSelected()).isFalse();
            assertThat(viewModel.getChildren()).isNullOrEmpty();
        });
    }

    @Test
    public void keepsOrderFromCategoryTree() throws Exception {
        test(CAT_B, ALL_CATEGORY_TREE, asList(TERM_D, TERM_C), viewModel ->
                assertThat(viewModel.getChildren())
                        .extracting(FormSelectableOptionViewModel::getLabel)
                        .containsExactly("C", "D"));
    }

    @Test
    public void inheritsInformationFromLeaves() throws Exception {
        test(CAT_B, ALL_CATEGORY_TREE, asList(TERM_D, TERM_C, TERM_B), viewModel -> {
            assertThat(viewModel.getLabel()).isEqualTo("B");
            assertThat(viewModel.getCount()).isEqualTo(6);
            assertThat(viewModel.isSelected()).isTrue();
            assertThat(viewModel.getChildren())
                    .extracting(FormSelectableOptionViewModel::getLabel)
                    .containsExactly("C", "D");
        });
    }

    @Test
    public void discardsEmptyLeaves() throws Exception {
        test(CAT_B, ALL_CATEGORY_TREE, asList(TERM_C, TERM_B), viewModel ->
                assertThat(viewModel.getChildren())
                        .extracting(FormSelectableOptionViewModel::getLabel)
                        .containsExactly("C"));
    }

    @Test
    public void discardsEmptyBranches() throws Exception {
        test(CAT_A, ALL_CATEGORY_TREE, asList(TERM_D, TERM_C, TERM_B), viewModelA -> {
            assertThat(viewModelA.getLabel()).isEqualTo("A");
            assertThat(viewModelA.getCount()).isEqualTo(6);
            assertThat(viewModelA.isSelected()).isTrue();
            assertThat(viewModelA.getChildren()).hasSize(1);

            final FacetOptionViewModel viewModelB = viewModelA.getChildren().get(0);
            assertThat(viewModelB.getLabel()).isEqualTo("B");
            assertThat(viewModelB.getCount()).isEqualTo(6);
            assertThat(viewModelB.isSelected()).isTrue();
            assertThat(viewModelB.getChildren())
                    .extracting(FormSelectableOptionViewModel::getLabel)
                    .containsExactly("C", "D");
        });
    }

    private void test(final Category category, final CategoryTree categoryTree, final List<TermStats> termStats, final Consumer<FacetOptionViewModel> test) {
        final UserLanguage userLanguage = mock(UserLanguage.class);
        when(userLanguage.locales()).thenReturn(singletonList(Locale.ENGLISH));
        final CategoryTreeFacetOptionViewModelFactory factory = new CategoryTreeFacetOptionViewModelFactory(userLanguage, categoryTree, reverseRouter());
        final TermFacetResult termFacetResult = TermFacetResult.of(0L, 0L, 0L, termStats);
        test.accept(factory.create(termFacetResult, category, CAT_C));
    }

    private static Category category(final String id, @Nullable final String parentId, final String name) {
        final Category category = mock(Category.class);
        when(category.getId()).thenReturn(id);
        when(category.getName()).thenReturn(LocalizedString.ofEnglish(name));
        when(category.getSlug()).thenReturn(LocalizedString.ofEnglish(name + "-slug"));
        when(category.getParent()).thenReturn(parentId != null ? Category.reference(parentId) : null);
        return category;
    }

    private static ProductReverseRouter reverseRouter() {
        final ProductReverseRouter productReverseRouter = mock(ProductReverseRouter.class);
        when(productReverseRouter.productOverviewPageCall(any(Category.class)))
                .then(invocation -> ((Category) invocation.getArgument(0)).getSlug()
                        .find(Locale.ENGLISH)
                        .map(TestableCall::new));
        return productReverseRouter;
    }
}
