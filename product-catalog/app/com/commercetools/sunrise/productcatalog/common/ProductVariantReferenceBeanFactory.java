package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductSimpleReverseRouter;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class ProductVariantReferenceBeanFactory extends ViewModelFactory<ProductVariantReferenceBean, ProductWithVariant> {

    private final Locale locale;
    private final ProductSimpleReverseRouter productReverseRouter;

    @Inject
    public ProductVariantReferenceBeanFactory(final Locale locale, final ProductSimpleReverseRouter productReverseRouter) {
        this.locale = locale;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    protected ProductVariantReferenceBean getViewModelInstance() {
        return new ProductVariantReferenceBean();
    }

    @Override
    public final ProductVariantReferenceBean create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductVariantReferenceBean model, final ProductWithVariant data) {
        fillId(model, data);
        fillUrl(model, data);
    }

    protected void fillId(final ProductVariantReferenceBean model, final ProductWithVariant productWithVariant) {
        model.setId(productWithVariant.getVariant().getId());
    }

    protected void fillUrl(final ProductVariantReferenceBean model, final ProductWithVariant productWithVariant) {
        model.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, productWithVariant.getProduct(), productWithVariant.getVariant()));
    }
}
