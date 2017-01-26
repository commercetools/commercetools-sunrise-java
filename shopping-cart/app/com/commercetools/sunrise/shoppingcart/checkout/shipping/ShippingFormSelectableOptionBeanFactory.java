package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableOptionViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Location;
import io.sphere.sdk.zones.Zone;
import io.sphere.sdk.zones.queries.ZoneQuery;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateApplicableShippingCosts;
import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public class ShippingFormSelectableOptionBeanFactory extends SelectableOptionViewModelFactory<ShippingFormSelectableOptionBean, ShippingMethod> {

    private final Location location;
    private final PriceFormatter priceFormatter;
    private final SphereClient sphereClient;

    @Inject
    public ShippingFormSelectableOptionBeanFactory(final CountryCode country, final PriceFormatter priceFormatter) {
        this.location = Location.of(country);
        this.priceFormatter = priceFormatter;
    }

    @Override
    protected ShippingFormSelectableOptionBean getViewModelInstance() {
        return new ShippingFormSelectableOptionBean();
    }

    @Override
    public final ShippingFormSelectableOptionBean create(final ShippingMethod option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
        fillDescription(model, option, selectedValue);
        fillPrice(model, option, selectedValue);
    }

    protected void fillLabel(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setLabel(option.getName());
    }

    protected void fillValue(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setValue(option.getId());
    }

    protected void fillSelected(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setSelected(option.getId().equals(selectedValue));
    }

    protected void fillDescription(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        model.setDescription(option.getDescription());
    }

    protected void fillPrice(final ShippingFormSelectableOptionBean model, final ShippingMethod option, @Nullable final String selectedValue) {
        findShippingMethodPrice(cart, shippingMethod).ifPresent(model::setPrice);
    }

    private Optional<Zone> fetchZone() {
        // Need to do this since zones are not expanded in shipping methods yet (but will be soon)
        // Rather this (even though it's expensive -one request per shipping method-) but it will mean less breaking changes in the future
        final ZoneQuery request = ZoneQuery.of().byLocation(location);
        final CompletionStage<Optional<Zone>> zoneStage = sphereClient.execute(request)
                .thenApplyAsync(PagedResult::head);
        return blockingWait(zoneStage, Duration.ofMinutes(1));
    }

    private Optional<String> findShippingMethodPrice(final ShippingMethod shippingMethod) {
        return fetchZone()
                .flatMap(zone -> shippingMethod.getShippingRatesForZone(zone).stream()
                        .filter(shippingRate -> shippingRate.getPrice().getCurrency().equals(cart.getCurrency()))
                        .findAny()
                        .map(shippingRate -> priceFormatter.format(calculateApplicableShippingCosts(cart, shippingRate))));
    }
}
