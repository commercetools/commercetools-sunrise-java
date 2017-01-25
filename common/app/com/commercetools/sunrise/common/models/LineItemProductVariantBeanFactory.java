package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class LineItemProductVariantBeanFactory extends AbstractProductVariantBeanFactory<LineItem> {

    private final Locale locale;
    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public LineItemProductVariantBeanFactory(final LocalizedStringResolver localizedStringResolver, final Locale locale,
                                             final PriceFormatter priceFormatter, final ProductReverseRouter productReverseRouter) {
        super(localizedStringResolver);
        this.locale = locale;
        this.priceFormatter = priceFormatter;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    public final ProductVariantBean create(final LineItem data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductVariantBean model, final LineItem data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillSku(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setSku(createSku(lineItem.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setName(createName(lineItem.getName()));
    }

    @Override
    protected void fillUrl(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, lineItem));
    }

    @Override
    protected void fillImage(final ProductVariantBean bean, final LineItem lineItem) {
        createImageUrl(lineItem.getVariant()).ifPresent(bean::setImage);
    }

    @Override
    protected void fillPrice(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setPrice(priceFormatter.format(ProductPriceUtils.calculateAppliedProductPrice(lineItem)));
    }

    @Override
    protected void fillPriceOld(final ProductVariantBean bean, final LineItem lineItem) {
        ProductPriceUtils.calculatePreviousProductPrice(lineItem)
                .ifPresent(oldPrice -> bean.setPriceOld(priceFormatter.format(oldPrice)));
    }
}