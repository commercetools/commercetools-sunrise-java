package productcatalog.common;

import com.google.inject.Inject;
import common.contexts.UserContext;
import common.models.LinkBean;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import wedecidelatercommon.ProductReverseRouter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class BreadcrumbBeanFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private ProductReverseRouter productReverseRouter;

    public BreadcrumbBean create(final ProductProjection product, final ProductVariant variant) {
        final BreadcrumbBean breadcrumbBean = new BreadcrumbBean();
        final List<LinkBean> linkBeans = product.getCategories().stream()
                .findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
        linkBeans.add(createProductLinkData(product, variant));
        breadcrumbBean.setLinks(linkBeans);
        return breadcrumbBean;
    }

    public BreadcrumbBean create(final Category category) {
        final BreadcrumbBean breadcrumbBean = new BreadcrumbBean();
        breadcrumbBean.setLinks(createCategoryTreeLinks(category));
        return breadcrumbBean;
    }

    public BreadcrumbBean create(final String searchTerm) {
        final BreadcrumbBean breadcrumbBean = new BreadcrumbBean();
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(searchTerm);
        breadcrumbBean.setLinks(singletonList(linkBean));
        return breadcrumbBean;
    }

    private List<LinkBean> createCategoryTreeLinks(final Category category) {
        return getCategoryWithAncestors(category).stream()
                .map(this::createCategoryLinkData)
                .collect(toList());
    }

    private LinkBean createCategoryLinkData(final Category category) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(category.getName().find(userContext.locales()).orElse(""));
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(userContext.locale(), category));
        return linkBean;
    }

    private LinkBean createProductLinkData(final ProductProjection currentProduct, final ProductVariant variant) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(currentProduct.getName().find(userContext.locales()).orElse(""));
        linkBean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), currentProduct, variant));
        return linkBean;
    }

    private List<Category> getCategoryWithAncestors(final Category category) {
        final List<Category> ancestors = category.getAncestors().stream()
                .map(ref -> categoryTree.findById(ref.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        ancestors.add(category);
        return ancestors;
    }
}
