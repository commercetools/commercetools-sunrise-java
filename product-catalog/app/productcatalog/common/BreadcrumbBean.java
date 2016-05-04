package productcatalog.common;

import common.contexts.UserContext;
import common.models.LinkBean;
import common.controllers.ReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class BreadcrumbBean {
    private List<LinkBean> links;

    public BreadcrumbBean() {
    }

    public BreadcrumbBean(final String searchTerm) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(searchTerm);
        this.links = singletonList(linkBean);
    }

    public BreadcrumbBean(final Category currentCategory, final CategoryTree categoryTree, final UserContext userContext,
                          final ReverseRouter reverseRouter) {
        this.links = createCategoryBreadcrumb(currentCategory, categoryTree, userContext, reverseRouter);
    }

    public BreadcrumbBean(final ProductProjection currentProduct, final ProductVariant currentVariant,
                          final CategoryTree categoryTree, final UserContext userContext, final ReverseRouter reverseRouter) {
        this.links = currentProduct.getCategories().stream().findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                                .map(currentCategory -> createCategoryBreadcrumb(currentCategory, categoryTree, userContext, reverseRouter))
                ).orElseGet(Collections::emptyList);
        this.links.add(createProductLinkData(currentProduct, currentVariant, userContext, reverseRouter));
    }

    public List<LinkBean> getLinks() {
        return links;
    }

    public void setLinks(final List<LinkBean> links) {
        this.links = links;
    }

    private static List<LinkBean> createCategoryBreadcrumb(final Category currentCategory, final CategoryTree categoryTree,
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

    private static LinkBean createCategoryLinkData(final Category category, final UserContext userContext,
                                                   final ReverseRouter reverseRouter) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(category.getName().find(userContext.locales()).orElse(""));
        linkBean.setUrl(reverseRouter.showCategoryUrlOrEmpty(userContext.locale(), category));
        return linkBean;
    }

    private static LinkBean createProductLinkData(final ProductProjection currentProduct, final ProductVariant variant,
                                                  final UserContext userContext, final ReverseRouter reverseRouter) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(currentProduct.getName().find(userContext.locales()).orElse(""));
        linkBean.setUrl(reverseRouter.showProductUrlOrEmpty(userContext.locale(), currentProduct, variant));
        return linkBean;
    }
}
