package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductBeanFactory extends ViewModelFactory {

    private final LocalizedStringResolver localizedStringResolver;
    private final ProductVariantBeanFactory productVariantBeanFactory;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;
    private final SelectableProductAttributeBeanFactory selectableProductAttributeBeanFactory;
    private final ProductBeanWithAttributeCombinationFactory productBeanWithAttributeCombinationFactory;

    @Inject
    public ProductBeanFactory(final LocalizedStringResolver localizedStringResolver, final ProductVariantBeanFactory productVariantBeanFactory,
                              final ProductAttributeBeanFactory productAttributeBeanFactory,
                              final SelectableProductAttributeBeanFactory selectableProductAttributeBeanFactory,
                              final ProductBeanWithAttributeCombinationFactory productBeanWithAttributeCombinationFactory) {
        this.localizedStringResolver = localizedStringResolver;
        this.productVariantBeanFactory = productVariantBeanFactory;
        this.productAttributeBeanFactory = productAttributeBeanFactory;
        this.selectableProductAttributeBeanFactory = selectableProductAttributeBeanFactory;
        this.productBeanWithAttributeCombinationFactory = productBeanWithAttributeCombinationFactory;
    }

    public ProductBean create(final ProductProjection product, final ProductVariant variant) {
        final ProductBean bean = new ProductBean();
        initialize(bean, product, variant);
        return bean;
    }

    protected final void initialize(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        fillIds(bean, product, variant);
        fillDescription(bean, product, variant);
        fillGallery(bean, product, variant);
        fillDetails(bean, product, variant);
        fillVariant(bean, product, variant);
        fillAttributes(bean, product, variant);
        fillAvailability(bean, product, variant);
        fillAttributeCombination(bean, product, variant);
    }

    protected void fillAttributes(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setAttributes(selectableProductAttributeBeanFactory.createList(variant, product));
    }

    protected void fillAttributeCombination(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        productBeanWithAttributeCombinationFactory.fill(bean, product, variant);
    }

    protected void fillVariant(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setVariant(productVariantBeanFactory.create(product, variant));
    }

    protected void fillDetails(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setDetails(createProductDetails(variant));
    }

    protected void fillGallery(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setGallery(createGallery(variant));
    }

    protected void fillDescription(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setDescription(Optional.ofNullable(product.getDescription())
                .flatMap(localizedStringResolver::find)
                .orElse(""));
    }

    protected void fillIds(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setProductId(product.getId());
        bean.setVariantId(variant.getId());
    }

    protected void fillAvailability(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        Optional.ofNullable(variant.getAvailability())
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

    private ProductGalleryBean createGallery(final ProductVariant variant) {
        final ProductGalleryBean bean = new ProductGalleryBean();
        bean.setList(variant.getImages().stream()
                .map(this::createProductImage)
                .collect(toList()));
        return bean;
    }

    private ProductImageBean createProductImage(final Image image) {
        final ProductImageBean imageBean = new ProductImageBean();
        imageBean.setThumbImage(image.getUrl());
        imageBean.setBigImage(image.getUrl());
        return imageBean;
    }

    private ProductDetailsBean createProductDetails(final ProductVariant variant) {
        final ProductDetailsBean bean = new ProductDetailsBean();
        bean.setFeatures(productAttributeBeanFactory.createList(variant));
        return bean;
    }

}
