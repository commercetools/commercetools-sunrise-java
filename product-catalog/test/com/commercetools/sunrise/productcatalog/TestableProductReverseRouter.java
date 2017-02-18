package com.commercetools.sunrise.productcatalog;

import com.commercetools.sunrise.common.reverserouter.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.test.TestableCall;
import play.mvc.Call;

import java.util.Locale;

public class TestableProductReverseRouter implements ProductReverseRouter {

    @Override
    public String languageTag() {
        return "en";
    }

    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
        return new TestableCall(productSlug + sku);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
        return new TestableCall(categorySlug);
    }

    @Override
    public Call processSearchProductsForm(final String languageTag) {
        return new TestableCall("search");
    }
}
