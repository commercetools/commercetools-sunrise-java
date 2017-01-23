package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.ProductPriceUtils;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class ProductVariantBeanFactory extends ViewModelFactory<ProductVariantBean, ProductVariantBeanFactory.Data> {

    private final Locale locale;
    private final LocalizedStringResolver localizedStringResolver;
    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantBeanFactory(final Locale locale, final LocalizedStringResolver localizedStringResolver,
                                     final PriceFormatter priceFormatter, final ProductReverseRouter productReverseRouter) {
        this.locale = locale;
        this.localizedStringResolver = localizedStringResolver;
        this.priceFormatter = priceFormatter;
        this.productReverseRouter = productReverseRouter;
    }

    public final ProductVariantBean create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
        return initializedViewModel(data);
    }

    public final ProductVariantBean create(final LineItem lineItem) {
        final Data data = new Data(lineItem);
        return initializedViewModel(data);
    }

    @Override
    protected ProductVariantBean getViewModelInstance() {
        return new ProductVariantBean();
    }

    @Override
    protected final void initialize(final ProductVariantBean bean, final Data data) {
        fillSku(bean, data);
        fillName(bean, data);
        fillUrl(bean, data);
        fillImage(bean, data);
        fillPrice(bean, data);
        fillPriceOld(bean, data);
    }

    protected void fillSku(final ProductVariantBean bean, final Data data) {
        bean.setSku(data.variant.getSku());
    }

    protected void fillName(final ProductVariantBean bean, final Data data) {
        if (data.product != null) {
            bean.setName(createName(data.product.getName()));
        } else if (data.lineItem != null) {
            bean.setName(createName(data.lineItem.getName()));
        }
    }

    protected void fillUrl(final ProductVariantBean bean, final Data data) {
        if (data.product != null) {
            bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, data.product, data.variant));
        } else if (data.lineItem != null) {
            bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, data.lineItem));
        }
    }

    protected void fillImage(final ProductVariantBean bean, final Data data) {
        data.variant.getImages().stream().findFirst().map(Image::getUrl).ifPresent(bean::setImage);
    }

    protected void fillPrice(final ProductVariantBean bean, final Data data) {
        if (data.lineItem != null) {
            bean.setPrice(priceFormatter.format(ProductPriceUtils.calculateAppliedProductPrice(data.lineItem)));
        } else {
            ProductPriceUtils.calculateAppliedProductPrice(data.variant)
                    .ifPresent(price -> bean.setPrice(priceFormatter.format(price)));
        }
    }

    protected void fillPriceOld(final ProductVariantBean bean, final Data data) {
        if (data.lineItem != null) {
            ProductPriceUtils.calculatePreviousProductPrice(data.lineItem)
                    .ifPresent(oldPrice -> bean.setPriceOld(priceFormatter.format(oldPrice)));
        } else {
            ProductPriceUtils.calculatePreviousProductPrice(data.variant)
                    .ifPresent(oldPrice -> bean.setPriceOld(priceFormatter.format(oldPrice)));
        }
    }

    private String createName(final LocalizedString name) {
        return localizedStringResolver.getOrEmpty(name);
    }

    protected final static class Data extends Base {

        @Nullable
        public final ProductProjection product;
        @Nullable
        public final LineItem lineItem;
        public final ProductVariant variant;

        public Data(final ProductProjection product, final ProductVariant variant) {
            this.product = product;
            this.lineItem = null;
            this.variant = variant;
        }

        public Data(final LineItem lineItem) {
            this.product = null;
            this.lineItem = lineItem;
            this.variant = lineItem.getVariant();
        }
    }
}