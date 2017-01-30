package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.controllers.TestableReverseRouter;
import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.commercetools.sunrise.common.utils.JsonUtils.readCtpObject;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryBreadcrumbBeanFactoryTest {

    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(readCtpObject("breadcrumb/breadcrumbCategories.json", CategoryQuery.resultTypeReference()).getResults());
    private static final ProductReverseRouter REVERSE_ROUTER = reverseRouter();

    @Test
    public void createCategoryBreadcrumbOfOneLevel() {
        testCategoryBreadcrumb("1stLevel",
                texts -> assertThat(texts).containsExactly("1st Level"),
                urls -> assertThat(urls).containsExactly("category-1st-level"));
    }

    @Test
    public void createCategoryBreadcrumbOfManyLevels() {
        testCategoryBreadcrumb("3rdLevel",
                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "3rd Level"),
                urls -> assertThat(urls).containsExactly("category-1st-level", "category-2nd-level", "category-3rd-level"));
    }

    private void testCategoryBreadcrumb(final String extId, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        final Category category = CATEGORY_TREE.findByExternalId(extId).get();
        final ProductOverviewControllerData data = new ProductOverviewControllerData(null, category);
        final BreadcrumbBean breadcrumb = createBreadcrumbFactory().create(data);
        testBreadcrumb(breadcrumb, texts, urls);
    }

    private static CategoryBreadcrumbBeanFactory createBreadcrumbFactory() {
        final LocalizedStringResolver dummyResolver = localizedString -> localizedString.find(Locale.ENGLISH);
        return new CategoryBreadcrumbBeanFactory(Locale.ENGLISH, CATEGORY_TREE, dummyResolver, REVERSE_ROUTER);
    }

    private void testBreadcrumb(final BreadcrumbBean breadcrumb, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        texts.accept(breadcrumb.getLinks().stream().map(LinkBean::getText).collect(toList()));
        urls.accept(breadcrumb.getLinks().stream().map(LinkBean::getUrl).collect(toList()));
    }

    private static ProductReverseRouter reverseRouter() {
        final TestableReverseRouter reverseRouter = new TestableReverseRouter();
        reverseRouter.setShowCategoryUrl("category-");
        return reverseRouter;
    }
}
