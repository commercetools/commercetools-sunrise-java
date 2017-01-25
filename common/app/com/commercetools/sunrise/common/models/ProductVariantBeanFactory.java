package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class ProductVariantBeanFactory extends AbstractProductVariantBeanFactory<ProductWithVariant> {

    private final Locale locale;
    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantBeanFactory(final LocalizedStringResolver localizedStringResolver, final Locale locale,
                                     final PriceFormatter priceFormatter, final ProductReverseRouter productReverseRouter) {
        super(localizedStringResolver);
        this.locale = locale;
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
        model.setSku(createSku(productWithVariant.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        model.setName(createName(productWithVariant.getProduct().getName()));
    }

    @Override
    protected void fillUrl(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        model.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, productWithVariant.getProduct(), productWithVariant.getVariant()));
    }

    @Override
    protected void fillImage(final ProductVariantBean model, final ProductWithVariant productWithVariant) {
        createImageUrl(productWithVariant.getVariant()).ifPresent(model::setImage);
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