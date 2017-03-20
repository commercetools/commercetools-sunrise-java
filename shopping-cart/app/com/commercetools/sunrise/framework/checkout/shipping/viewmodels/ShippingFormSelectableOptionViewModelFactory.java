package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.framework.viewmodels.forms.SelectableViewModelFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Location;
import io.sphere.sdk.zones.Zone;
import io.sphere.sdk.zones.queries.ZoneQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.commercetools.sunrise.framework.viewmodels.content.carts.CartPriceUtils.calculateApplicableShippingCosts;

@RequestScoped
public class ShippingFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<ShippingFormSelectableOptionViewModel, ShippingMethod, String> {

    private final Logger LOGGER = LoggerFactory.getLogger(ShippingFormSelectableOptionViewModelFactory.class);

    private final CartFinder cartFinder;
    private final PriceFormatter priceFormatter;
    private final SphereClient sphereClient;

    @Inject
    public ShippingFormSelectableOptionViewModelFactory(final CartFinder cartFinder, final PriceFormatter priceFormatter, final SphereClient sphereClient) {
        this.cartFinder = cartFinder;
        this.priceFormatter = priceFormatter;
        this.sphereClient = sphereClient;
    }

    protected final PriceFormatter getPriceFormatter() {
        return priceFormatter;
    }

    @Override
    protected ShippingFormSelectableOptionViewModel newViewModelInstance(final ShippingMethod option, final String selectedValue) {
        return new ShippingFormSelectableOptionViewModel();
    }

    @Override
    public final ShippingFormSelectableOptionViewModel create(final ShippingMethod option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ShippingFormSelectableOptionViewModel viewModel, final ShippingMethod option, @Nullable final String selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
        fillDescription(viewModel, option, selectedValue);
        fillPrice(viewModel, option, selectedValue);
    }

    protected void fillLabel(final ShippingFormSelectableOptionViewModel viewModel, final ShippingMethod option, @Nullable final String selectedValue) {
        viewModel.setLabel(option.getName());
    }

    protected void fillValue(final ShippingFormSelectableOptionViewModel viewModel, final ShippingMethod option, @Nullable final String selectedValue) {
        viewModel.setValue(option.getId());
    }

    protected void fillSelected(final ShippingFormSelectableOptionViewModel viewModel, final ShippingMethod option, @Nullable final String selectedValue) {
        viewModel.setSelected(option.getId().equals(selectedValue));
    }

    protected void fillDescription(final ShippingFormSelectableOptionViewModel viewModel, final ShippingMethod option, @Nullable final String selectedValue) {
        viewModel.setDescription(option.getDescription());
    }

    protected void fillPrice(final ShippingFormSelectableOptionViewModel viewModel, final ShippingMethod option, @Nullable final String selectedValue) {
        // Need to do this since zones are not expanded in shipping methods yet (but will be soon)
        // Rather this (even though it's expensive -two requests per shipping method-) but it will mean less breaking changes in the future
        findCart()
                .ifPresent(cart -> findLocation(cart)
                        .ifPresent(location -> fetchZone(location)
                                .ifPresent(zone -> findShippingMethodPrice(option, zone, cart)
                                        .ifPresent(viewModel::setPrice))));
    }

    private Optional<Zone> fetchZone(final Location location) {
        final ZoneQuery request = ZoneQuery.of().byLocation(location);
        try {
            return sphereClient.execute(request)
                    .thenApplyAsync(PagedResult::head)
                    .toCompletableFuture().get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Could not fetch zone", e);
            return Optional.empty();
        }
    }

    private Optional<Cart> findCart() {
        try {
            return cartFinder.get().toCompletableFuture().get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Could not fetch cart", e);
            return Optional.empty();
        }
    }

    private Optional<Location> findLocation(final Cart cart) {
        return Optional.ofNullable(cart.getShippingAddress())
                .map(address -> Location.of(address.getCountry(), address.getState()));
    }

    private Optional<String> findShippingMethodPrice(final ShippingMethod shippingMethod, final Zone zone, final Cart cart) {
        return shippingMethod.getShippingRatesForZone(zone).stream()
                .filter(shippingRate -> shippingRate.getPrice().getCurrency().equals(cart.getCurrency()))
                .findAny()
                .map(shippingRate -> priceFormatter.format(calculateApplicableShippingCosts(cart, shippingRate)));
    }
}
