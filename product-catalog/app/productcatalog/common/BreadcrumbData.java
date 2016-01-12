package productcatalog.common;

import common.contexts.UserContext;
import common.models.LinkData;
import common.controllers.ReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class BreadcrumbData {
    private List<LinkData> links;

    public BreadcrumbData() {
    }

    public BreadcrumbData(final String searchTerm) {
        final LinkData linkData = new LinkData();
        linkData.setText(searchTerm);
        this.links = singletonList(linkData);
    }

    public BreadcrumbData(final Category currentCategory, final CategoryTree categoryTree, final UserContext userContext,
                          final ReverseRouter reverseRouter) {
        this.links = createCategoryBreadcrumb(currentCategory, categoryTree, userContext, reverseRouter);
    }

    public BreadcrumbData(final ProductProjection currentProduct, final ProductVariant currentVariant,
                          final CategoryTree categoryTree, final UserContext userContext, final ReverseRouter reverseRouter) {
        this.links = currentProduct.getCategories().stream().findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                                .map(currentCategory -> createCategoryBreadcrumb(currentCategory, categoryTree, userContext, reverseRouter))
                ).orElse(emptyList());
        this.links.add(createProductLinkData(currentProduct, currentVariant, userContext, reverseRouter));
    }

    public List<LinkData> getLinks() {
        return links;
    }

    public void setLinks(final List<LinkData> links) {
        this.links = links;
    }

    private static List<LinkData> createCategoryBreadcrumb(final Category currentCategory, final CategoryTree categoryTree,
                                                           final UserContext userContext, final ReverseRouter reverseRouter) {
        final List<Category> categoryWithAncestors = getCategoryWithAncestors(currentCategory, categoryTree);
        return categoryWithAncestors.stream()
                .map(category -> createCategoryLinkData(category, userContext, reverseRouter))
                .collect(toList());
    }

    private static List<Category> getCategoryWithAncestors(final Category category, final CategoryTree categoryTree) {
        final List<Category> ancestors = category.getAncestors().stream()
                .map(ref -> categoryTree.findById(ref.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        ancestors.add(category);
        return ancestors;
    }

    private static LinkData createCategoryLinkData(final Category category, final UserContext userContext,
                                                   final ReverseRouter reverseRouter) {
        final LinkData linkData = new LinkData();
        linkData.setText(category.getName().find(userContext.locales()).orElse(""));
        linkData.setUrl(getCategoryUrl(category, userContext.locale(), reverseRouter));
        return linkData;
    }

    private static LinkData createProductLinkData(final ProductProjection currentProduct, final ProductVariant variant,
                                                  final UserContext userContext, final ReverseRouter reverseRouter) {
        final LinkData linkData = new LinkData();
        linkData.setText(currentProduct.getName().find(userContext.locales()).orElse(""));
        linkData.setUrl(getProductUrl(currentProduct, variant, userContext.locale(), reverseRouter));
        return linkData;
    }

    private static String getCategoryUrl(final Category category, final Locale locale, final ReverseRouter reverseRouter) {
        final String slug = category.getSlug().find(locale).orElse("");
        return reverseRouter.category(locale.toLanguageTag(), slug).url();
    }

    private static String getProductUrl(final ProductProjection product, final ProductVariant variant,
                                        final Locale locale, final ReverseRouter reverseRouter) {
        final String slug = product.getSlug().find(locale).orElse("");
        return reverseRouter.product(locale.toLanguageTag(), slug, variant.getSku()).url();
    }
}
