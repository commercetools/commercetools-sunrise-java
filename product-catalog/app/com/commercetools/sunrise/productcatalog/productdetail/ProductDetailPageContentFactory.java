package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.ProductBeanFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;

public class ProductDetailPageContentFactory extends Base {

    @Inject
    protected UserContext userContext;
    @Inject
    protected CartReverseRouter cartReverseRouter;
    @Inject
    protected BreadcrumbBeanFactory breadcrumbBeanFactory;
    @Inject
    protected ProductBeanFactory productBeanFactory;

    public ProductDetailPageContent create(final ProductProjection product, final ProductVariant variant) {
        final ProductDetailPageContent bean = new ProductDetailPageContent();
        initialize(bean, product, variant);
        return bean;
    }

    protected final void initialize(final ProductDetailPageContent bean, final ProductProjection product, final ProductVariant variant) {
        fillAdditionalTitle(product, bean);
        fillProduct(product, variant, bean);
        fillBreadCrumb(product, variant, bean);
        fillAddToCartFormUrl(bean);
    }

    protected void fillAddToCartFormUrl(final ProductDetailPageContent content) {
        content.setAddToCartFormUrl(getAddToCartUrl()); // TODO move to page meta
    }

    protected void fillAdditionalTitle(final ProductProjection product, final ProductDetailPageContent content) {
        content.setTitle(getAdditionalTitle(product));
    }

    protected void fillBreadCrumb(final ProductProjection product, final ProductVariant variant, final ProductDetailPageContent content) {
        content.setBreadcrumb(breadcrumbBeanFactory.create(product, variant));
    }

    protected void fillProduct(final ProductProjection product, final ProductVariant variant, final ProductDetailPageContent content) {
        content.setProduct(productBeanFactory.create(product, variant));
    }

    protected String getAdditionalTitle(final ProductProjection product) {
        return product.getName().find(userContext.locales()).orElse("");
    }

    protected String getAddToCartUrl() {
        return cartReverseRouter.processAddProductToCartForm(userContext.locale().getLanguage()).url();
    }
}
