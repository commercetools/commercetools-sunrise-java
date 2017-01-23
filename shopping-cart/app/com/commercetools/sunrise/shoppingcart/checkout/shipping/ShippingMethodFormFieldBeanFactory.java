package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.shoppingcart.ZoneFinderByCart;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Zone;
import play.Configuration;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.commercetools.sunrise.common.forms.FormUtils.extractFormField;
import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateApplicableShippingCosts;
import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class ShippingMethodFormFieldBeanFactory extends ViewModelFactory {

    private final String formFieldName;
    private final PriceFormatter priceFormatter;
    private final ZoneFinderByCart zoneFinderByCart;

    @Inject
    public ShippingMethodFormFieldBeanFactory(final PriceFormatter priceFormatter, final ZoneFinderByCart zoneFinderByCart,
                                              final Configuration configuration) {
        this.formFieldName = configuration.getString("checkout.shipping.formFieldName", "shippingMethodId");
        this.priceFormatter = priceFormatter;
        this.zoneFinderByCart = zoneFinderByCart;
    }

    public ShippingMethodFormFieldBean create(final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final ShippingMethodFormFieldBean bean = new ShippingMethodFormFieldBean();
        initialize(bean, form, cart, shippingMethods);
        return bean;
    }

    protected final void initialize(final ShippingMethodFormFieldBean bean, final Form<?> form,
                                    final Cart cart, final List<ShippingMethod> shippingMethods) {
        fillList(bean, form, cart, shippingMethods);
    }

    protected void fillList(final ShippingMethodFormFieldBean bean, final Form<?> form,
                            final Cart cart, final List<ShippingMethod> shippingMethods) {
        final String selectedShippingMethodId = extractFormField(form, formFieldName);
        bean.setList(shippingMethods.stream()
                .map(shippingMethod -> createFormOption(cart, shippingMethod, selectedShippingMethodId))
                .collect(toList()));
    }

    protected ShippingFormSelectableOptionBean createFormOption(final Cart cart, final ShippingMethod shippingMethod,
                                                                @Nullable final String selectedShippingMethodId) {
        final ShippingFormSelectableOptionBean bean = new ShippingFormSelectableOptionBean();
        initializeFormOption(bean, cart, shippingMethod, selectedShippingMethodId);
        return bean;
    }

    protected final void initializeFormOption(final ShippingFormSelectableOptionBean bean, final Cart cart, final ShippingMethod shippingMethod,
                                              final @Nullable String selectedShippingMethodId) {
        fillFormOptionLabel(bean, shippingMethod);
        fillFormOptionValue(bean, shippingMethod);
        fillFormOptionSelected(bean, shippingMethod, selectedShippingMethodId);
        fillFormOptionDescription(bean, shippingMethod);
        fillFormOptionPrice(bean, cart, shippingMethod);
    }

    protected void fillFormOptionPrice(final ShippingFormSelectableOptionBean bean, final Cart cart, final ShippingMethod shippingMethod) {
        findShippingMethodPrice(cart, shippingMethod).ifPresent(bean::setPrice);
    }

    protected void fillFormOptionDescription(final ShippingFormSelectableOptionBean bean, final ShippingMethod shippingMethod) {
        bean.setDescription(shippingMethod.getDescription());
    }

    protected void fillFormOptionSelected(final ShippingFormSelectableOptionBean bean, final ShippingMethod shippingMethod, final @Nullable String selectedShippingMethodId) {
        bean.setSelected(shippingMethod.getId().equals(selectedShippingMethodId));
    }

    protected void fillFormOptionValue(final ShippingFormSelectableOptionBean bean, final ShippingMethod shippingMethod) {
        bean.setValue(shippingMethod.getId());
    }

    protected void fillFormOptionLabel(final ShippingFormSelectableOptionBean bean, final ShippingMethod shippingMethod) {
        bean.setLabel(shippingMethod.getName());
    }

    private Optional<String> findShippingMethodPrice(final Cart cart, final ShippingMethod shippingMethod) {
        return getZone(cart)
                .flatMap(zone -> shippingMethod.getShippingRatesForZone(zone).stream()
                        .filter(shippingRate -> shippingRate.getPrice().getCurrency().equals(cart.getCurrency()))
                        .findAny()
                        .map(shippingRate -> priceFormatter.format(calculateApplicableShippingCosts(cart, shippingRate))));
    }

    private Optional<Zone> getZone(final Cart cart) {
        // Need to do this since zones are not expanded in shipping methods yet (but will be soon)
        // Rather this (even though it's expensive -one request per shipping method-) but it will mean less breaking changes in the future
        return blockingWait(zoneFinderByCart.findZone(cart), Duration.ofMinutes(1));
    }
}
