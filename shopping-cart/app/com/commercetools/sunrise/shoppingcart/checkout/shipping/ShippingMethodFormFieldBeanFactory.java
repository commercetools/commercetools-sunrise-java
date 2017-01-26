package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import com.commercetools.sunrise.common.models.FormFieldViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Zone;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateApplicableShippingCosts;
import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class ShippingMethodFormFieldBeanFactory extends FormFieldViewModelFactory<ShippingMethodFormFieldBean, ShippingMethod> {

    private final PriceFormatter priceFormatter;
    private final ZoneFinderByCart zoneFinderByCart;

    @Inject
    public ShippingMethodFormFieldBeanFactory(final PriceFormatter priceFormatter, final ZoneFinderByCart zoneFinderByCart) {
        this.priceFormatter = priceFormatter;
        this.zoneFinderByCart = zoneFinderByCart;
    }

    @Override
    protected ShippingMethodFormFieldBean getViewModelInstance() {
        return new ShippingMethodFormFieldBean();
    }

    @Override
    protected List<ShippingMethod> defaultOptions() {
        return Collections.emptyList();
    }

    @Override
    public final ShippingMethodFormFieldBean create(final FormFieldWithOptions<ShippingMethod> data) {
        return super.create(data);
    }

    @Override
    public final ShippingMethodFormFieldBean createWithDefaultOptions(final Form.Field formField) {
        return super.createWithDefaultOptions(formField);
    }

    @Override
    protected void initialize(final ShippingMethodFormFieldBean model, final FormFieldWithOptions<ShippingMethod> data) {
        fillList(model, data);
    }

    protected void fillList(final ShippingMethodFormFieldBean model, final FormFieldWithOptions<ShippingMethod> data) {
        model.setList(data.formOptions.stream()
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
