package com.commercetools.sunrise.framework.reverserouters.productcatalog.product;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleProductReverseRouterByReflection.class)
public interface SimpleProductReverseRouter {

    String PRODUCT_DETAIL_PAGE = "productDetailPageCall";

    Call productDetailPageCall(final String languageTag, final String productIdentifier, final String productVariantIdentifier);

    String PRODUCT_OVERVIEW_PAGE = "productOverviewPageCall";

    Call productOverviewPageCall(final String languageTag, final String categoryIdentifier);

    String SEARCH_PROCESS = "searchProcessCall";

    Call searchProcessCall(final String languageTag);
}
