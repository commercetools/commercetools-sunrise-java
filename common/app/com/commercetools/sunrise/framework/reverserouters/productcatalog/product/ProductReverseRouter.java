package com.commercetools.sunrise.framework.reverserouters.productcatalog.product;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(DefaultProductReverseRouter.class)
public interface ProductReverseRouter extends SimpleProductReverseRouter, LocalizedReverseRouter {

    /**
     * Returns the call to access the product detail page of the product and variant with the given identifiers.
     * @param productIdentifier identifies the product that we want to access via the page call
     * @param productVariantIdentifier identifies the particular variant of the product that we want to access via the page call
     * @return the page call to access the product detail page of this product
     */
    default Call productDetailPageCall(final String productIdentifier, final String productVariantIdentifier) {
        return productDetailPageCall(locale().toLanguageTag(), productIdentifier, productVariantIdentifier);
    }

    /**
     * Returns the call to access the product overview page for the products belonging to the category with the given identifier.
     * @param categoryIdentifier identifies the category to which the products that we want to access via the page call belong
     * @return the page call to access the product overview page of the products belonging to the given category.
     */
    default Call productOverviewPageCall(final String categoryIdentifier) {
        return productOverviewPageCall(locale().toLanguageTag(), categoryIdentifier);
    }

    /**
     * Returns the call to process the product search.
     * @return the process call for the search
     */
    default Call searchProcessCall() {
        return searchProcessCall(locale().toLanguageTag());
    }

    /**
     * Finds the call to access the product detail page of the given product and variant.
     * @param product the product that we want to access via the page call
     * @param productVariant the particular variant of the product that we want to access via the page call
     * @return the page call to access the product detail page of this product
     */
    Optional<Call> productDetailPageCall(final ProductProjection product, final ProductVariant productVariant);

    /**
     * Finds the call to access the product detail page of the product contained in the given line item.
     * @param lineItem the line item containing the product that we want to access via the page call
     * @return the page call to access the product detail page of the product contained in the line item
     */
    Optional<Call> productDetailPageCall(final LineItem lineItem);    /**

     * Finds the call to access the product detail page of the product contained in the given line item.
     * @param lineItem the line item containing the product that we want to access via the page call
     * @return the page call to access the product detail page of the product contained in the line item
     */
    Optional<Call> productDetailPageCall(final io.sphere.sdk.shoppinglists.LineItem lineItem);

    /**
     * Finds the call to access the product overview page for the products belonging to the given category.
     * @param category the category to which the products that we want to access via the page call belong
     * @return the page call to access the product overview page of the products belonging to the given category.
     */
    Optional<Call> productOverviewPageCall(final Category category);
}
