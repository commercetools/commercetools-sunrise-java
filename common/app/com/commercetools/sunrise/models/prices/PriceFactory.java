package com.commercetools.sunrise.models.prices;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Singleton;
import java.util.Optional;

import static com.commercetools.sunrise.models.products.ProductPriceUtils.calculateAppliedProductPrice;
import static com.commercetools.sunrise.models.products.ProductPriceUtils.calculatePreviousProductPrice;

@Singleton
public class PriceFactory extends ViewModelFactory {

    public Price create(final LineItem lineItem) {
        final Price price = new Price(calculateAppliedProductPrice(lineItem));
        calculatePreviousProductPrice(lineItem).ifPresent(price::setPrevious);
        return price;
    }

    public Optional<Price> create(final ProductVariant variant) {
        final Optional<Price> priceOpt = calculateAppliedProductPrice(variant).map(Price::new);
        priceOpt.ifPresent(price -> calculatePreviousProductPrice(variant).ifPresent(price::setPrevious));
        return priceOpt;
    }
}
