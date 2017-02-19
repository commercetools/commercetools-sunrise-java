package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.products.AbstractProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.products.ProductVariantBean;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;

import javax.inject.Inject;

@RequestScoped
public class ProductVariantBeanFactory extends AbstractProductVariantBeanFactory<ProductWithVariant> {

    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantBeanFactory(final PriceFormatter priceFormatter, final ProductReverseRouter productReverseRouter) {
        super();
        this.priceFormatter = priceFormatter;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    public final ProductVariantBean create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductVariantBean model, final ProductWithVariant data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillSku(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        model.setSku(findSku(productWithVariant.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        model.setName(productWithVariant.getProduct().getName());
    }

    @Override
    protected void fillUrl(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        model.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(productWithVariant.getProduct(), productWithVariant.getVariant()));
    }

    @Override
    protected void fillImage(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        findImageUrl(productWithVariant.getVariant()).ifPresent(model::setImage);
    }

    @Override
    protected void fillPrice(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        ProductPriceUtils.calculateAppliedProductPrice(productWithVariant.getVariant())
                .ifPresent(price -> model.setPrice(priceFormatter.format(price)));
    }

    @Override
    protected void fillPriceOld(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        ProductPriceUtils.calculatePreviousProductPrice(productWithVariant.getVariant())
                .ifPresent(oldPrice -> model.setPriceOld(priceFormatter.format(oldPrice)));
    }
}