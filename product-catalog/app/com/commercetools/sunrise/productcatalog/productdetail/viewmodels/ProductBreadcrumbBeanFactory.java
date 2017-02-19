package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.common.models.BreadcrumbBean;
import com.commercetools.sunrise.common.models.BreadcrumbLinkBean;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestScoped
public class ProductBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductWithVariant> {

    @Inject
    public ProductBreadcrumbBeanFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        super(categoryTree, productReverseRouter);
    }

    @Override
    public final BreadcrumbBean create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected final void initialize(final BreadcrumbBean model, final ProductWithVariant productWithVariant) {
        super.initialize(model, productWithVariant);
    }

    @Override
    protected void fillLinks(final BreadcrumbBean bean, final ProductWithVariant productWithVariant) {
        bean.setLinks(createProductLinks(productWithVariant));
    }

    protected List<BreadcrumbLinkBean> createProductLinks(final ProductWithVariant productWithVariant) {
        final List<BreadcrumbLinkBean> categoryTreeLinks = createCategoryTreeLinks(productWithVariant);
        final List<BreadcrumbLinkBean> result = new ArrayList<>(1 + categoryTreeLinks.size());
        result.addAll(categoryTreeLinks);
        result.add(createProductLink(productWithVariant));
        return result;
    }

    private List<BreadcrumbLinkBean> createCategoryTreeLinks(final ProductWithVariant productWithVariant) {
        return productWithVariant.getProduct().getCategories().stream()
                .findFirst()
                .flatMap(ref -> getCategoryTree().findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    private BreadcrumbLinkBean createProductLink(final ProductWithVariant productWithVariant) {
        final BreadcrumbLinkBean linkBean = new BreadcrumbLinkBean();
        linkBean.setText(productWithVariant.getProduct().getName());
        linkBean.setUrl(getProductReverseRouter().productDetailPageUrlOrEmpty(productWithVariant.getProduct(), productWithVariant.getVariant()));
        return linkBean;
    }
}
