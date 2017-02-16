package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class ProductBeanFactory extends ViewModelFactory<ProductBean, ProductWithVariant> {

    private final ProductAttributeSettings productAttributeSettings;
    private final ProductVariantBeanFactory productVariantBeanFactory;
    private final ProductDetailsBeanFactory productDetailsBeanFactory;
    private final ProductGalleryBeanFactory productGalleryBeanFactory;
    private final SelectableProductAttributeBeanFactory selectableProductAttributeBeanFactory;
    private final ProductVariantReferenceBeanMapFactory productVariantReferenceBeanMapFactory;

    @Inject
    public ProductBeanFactory(final ProductAttributeSettings productAttributeSettings, final ProductVariantBeanFactory productVariantBeanFactory,
                              final ProductDetailsBeanFactory productDetailsBeanFactory, final ProductGalleryBeanFactory productGalleryBeanFactory,
                              final SelectableProductAttributeBeanFactory selectableProductAttributeBeanFactory,
                              final ProductVariantReferenceBeanMapFactory productVariantReferenceBeanMapFactory) {
        this.productAttributeSettings = productAttributeSettings;
        this.productVariantBeanFactory = productVariantBeanFactory;
        this.productDetailsBeanFactory = productDetailsBeanFactory;
        this.productGalleryBeanFactory = productGalleryBeanFactory;
        this.selectableProductAttributeBeanFactory = selectableProductAttributeBeanFactory;
        this.productVariantReferenceBeanMapFactory = productVariantReferenceBeanMapFactory;
    }

    @Override
    protected ProductBean getViewModelInstance() {
        return new ProductBean();
    }

    @Override
    public final ProductBean create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductBean model, final ProductWithVariant data) {
        fillProductId(model, data);
        fillVariantId(model, data);
        fillDescription(model, data);
        fillGallery(model, data);
        fillDetails(model, data);
        fillVariant(model, data);
        fillAttributes(model, data);
        fillAvailability(model, data);
        fillVariantIdentifiers(model, data);
        fillAttributeCombination(model, data);
    }

    protected void fillProductId(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setProductId(productWithVariant.getProduct().getId());
    }

    protected void fillVariantId(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setVariantId(productWithVariant.getVariant().getId());
    }

    protected void fillDescription(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setDescription(productWithVariant.getProduct().getDescription());
    }

    protected void fillGallery(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setGallery(productGalleryBeanFactory.create(productWithVariant));
    }

    protected void fillDetails(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setDetails(productDetailsBeanFactory.create(productWithVariant));
    }

    protected void fillVariant(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setVariant(productVariantBeanFactory.create(productWithVariant));
    }

    protected void fillAttributes(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setAttributes(selectableProductAttributeBeanFactory.createList(productWithVariant));
    }

    protected void fillAttributeCombination(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setVariants(productVariantReferenceBeanMapFactory.create(productWithVariant));
    }

    protected void fillVariantIdentifiers(final ProductBean model, final ProductWithVariant productWithVariant) {
        model.setVariantIdentifiers(productAttributeSettings.getSelectableAttributes());
    }

    protected void fillAvailability(final ProductBean model, final ProductWithVariant productWithVariant) {
        Optional.ofNullable(productWithVariant.getVariant().getAvailability())
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
                    model.setAvailability(status);
                });
    }
}
