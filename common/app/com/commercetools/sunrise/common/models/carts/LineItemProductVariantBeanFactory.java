package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.products.AbstractProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.products.ProductVariantBean;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

@RequestScoped
public class LineItemProductVariantBeanFactory extends AbstractProductVariantBeanFactory<LineItem> {

    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public LineItemProductVariantBeanFactory(final PriceFormatter priceFormatter,
                                             final ProductReverseRouter productReverseRouter) {
        super();
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
        bean.setSku(findSku(lineItem.getVariant()));
    }

    @Override
    protected void fillName(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setName(lineItem.getName());
    }

    @Override
    protected void fillUrl(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(lineItem));
    }

    @Override
    protected void fillImage(final ProductVariantBean bean, final LineItem lineItem) {
        findImageUrl(lineItem.getVariant()).ifPresent(bean::setImage);
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