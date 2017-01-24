package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class ProductBeanFactory extends ViewModelFactory<ProductBean, ProductBeanFactory.Data> {

    private final LocalizedStringResolver localizedStringResolver;
    private final ProductDataConfig productDataConfig;
    private final ProductVariantBeanFactory productVariantBeanFactory;
    private final ProductDetailsBeanFactory productDetailsBeanFactory;
    private final ProductGalleryBeanFactory productGalleryBeanFactory;
    private final SelectableProductAttributeBeanFactory selectableProductAttributeBeanFactory;
    private final ProductVariantReferenceBeanMapFactory productVariantReferenceBeanMapFactory;

    @Inject
    public ProductBeanFactory(final LocalizedStringResolver localizedStringResolver, final ProductDataConfig productDataConfig,
                              final ProductVariantBeanFactory productVariantBeanFactory, final ProductDetailsBeanFactory productDetailsBeanFactory,
                              final ProductGalleryBeanFactory productGalleryBeanFactory, final SelectableProductAttributeBeanFactory selectableProductAttributeBeanFactory,
                              final ProductVariantReferenceBeanMapFactory productVariantReferenceBeanMapFactory) {
        this.localizedStringResolver = localizedStringResolver;
        this.productDataConfig = productDataConfig;
        this.productVariantBeanFactory = productVariantBeanFactory;
        this.productDetailsBeanFactory = productDetailsBeanFactory;
        this.productGalleryBeanFactory = productGalleryBeanFactory;
        this.selectableProductAttributeBeanFactory = selectableProductAttributeBeanFactory;
        this.productVariantReferenceBeanMapFactory = productVariantReferenceBeanMapFactory;
    }

    public final ProductBean create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
        return initializedViewModel(data);
    }

    @Override
    protected ProductBean getViewModelInstance() {
        return new ProductBean();
    }

    @Override
    protected final void initialize(final ProductBean bean, final Data data) {
        fillProductId(bean, data);
        fillVariantId(bean, data);
        fillDescription(bean, data);
        fillGallery(bean, data);
        fillDetails(bean, data);
        fillVariant(bean, data);
        fillAttributes(bean, data);
        fillAvailability(bean, data);
        fillVariantIdentifiers(bean, data);
        fillAttributeCombination(bean, data);
    }

    protected void fillProductId(final ProductBean bean, final Data data) {
        bean.setProductId(data.product.getId());
    }

    protected void fillVariantId(final ProductBean bean, final Data data) {
        bean.setVariantId(data.variant.getId());
    }

    protected void fillDescription(final ProductBean bean, final Data data) {
        bean.setDescription(Optional.ofNullable(data.product.getDescription())
                .flatMap(localizedStringResolver::find)
                .orElse(""));
    }

    protected void fillGallery(final ProductBean bean, final Data data) {
        bean.setGallery(productGalleryBeanFactory.create(data.product, data.variant));
    }

    protected void fillDetails(final ProductBean bean, final Data data) {
        bean.setDetails(productDetailsBeanFactory.create(data.product, data.variant));
    }

    protected void fillVariant(final ProductBean bean, final Data data) {
        bean.setVariant(productVariantBeanFactory.create(data.product, data.variant));
    }

    protected void fillAttributes(final ProductBean bean, final Data data) {
        bean.setAttributes(selectableProductAttributeBeanFactory.createList(data.product, data.variant));
    }

    protected void fillAttributeCombination(final ProductBean bean, final Data data) {
        bean.setVariants(productVariantReferenceBeanMapFactory.create(data.product, data.variant));
    }

    protected void fillVariantIdentifiers(final ProductBean bean, final Data data) {
        bean.setVariantIdentifiers(productDataConfig.getSelectableAttributes());
    }

    protected void fillAvailability(final ProductBean bean, final Data data) {
        Optional.ofNullable(data.variant.getAvailability())
                .flatMap(productVariantAvailability -> Optional.ofNullable(productVariantAvailability.getAvailableQuantity()))
                .ifPresent(quantity -> {
                    final String status;
                    if (quantity < 4) {
                        status = "notAvailable";
                    } else if (quantity > 10) {
                        status = "available";
                    } else {
                        status = "fewItemsLeft";
                    }
                    bean.setAvailability(status);
                });
    }

    protected final static class Data extends Base {

        public final ProductProjection product;
        public final ProductVariant variant;

        public Data(final ProductProjection product, final ProductVariant variant) {
            this.product = product;
            this.variant = variant;
        }
    }

}
