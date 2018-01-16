package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sdk.CtpEnumUtils;
import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.models.carts.CartPriceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.products.PriceUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.money.MonetaryAmount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class DefaultHandlebarsHelperSource implements HandlebarsHelperSource {

    private final I18nResolver i18nResolver;
    private final PriceFormatter priceFormatter;

    @Inject
    protected DefaultHandlebarsHelperSource(final I18nResolver i18nResolver, PriceFormatter priceFormatter) {
        this.i18nResolver = i18nResolver;
        this.priceFormatter = priceFormatter;
    }

    /**
     * Registrable {@code i18n} helper to obtain the correct translation for the given {@code messageKey}.
     * @param messageKey the message key to translate
     * @param options available options
     * @return the translated message
     */
    public CharSequence i18n(final String messageKey, final Options options) {
        return i18nResolver.getOrEmpty(messageKey, options.hash);
    }

    /**
     * Registrable {@code cms} helper to obtain the CMS content for the given {@code fieldName}.
     * @param fieldName the field name corresponding to the content
     * @param options available options
     * @return the content
     */
    public CharSequence cms(final String fieldName, final Options options) {
        final Object cms = options.context.get("cms");
        if (cms instanceof CmsPage) {
            return ((CmsPage) cms).field(fieldName).orElse(null);
        }
        return null;
    }

    /**
     * Registrable {@code json} helper to obtain the JSON string representation of a Java object.
     * @param object the Java object
     * @return the JSON string representation
     * @throws JsonProcessingException if the given object could not be processed into JSON
     */
    public String json(final Object object) throws JsonProcessingException {
        return SphereJsonUtils.toJsonString(object);
    }

    public CharSequence prettyJson(final Object object) throws JsonProcessingException {
        return SphereJsonUtils.prettyPrint(json(object));
    }

    public CharSequence formatPrice(final MonetaryAmount object) {
        return priceFormatter.format(object);
    }

    public CharSequence subTotal(final CartLike<?> object) {
        return formatPrice(object.calculateSubTotalPrice());
    }

    public CharSequence total(final CartLike<?> object) {
        return formatPrice(CartPriceUtils.calculateTotalPrice(object));
    }

    public CharSequence shippingCosts(final CartLike<?> object) {
        return CartPriceUtils.calculateAppliedShippingPrice(object).map(this::formatPrice).orElse("");
    }

    public CharSequence salesTaxCartLike(final CartLike<?> cartLike) {
        return formatPrice(cartLike.calculateTotalAppliedTaxes()
                .orElseGet(() -> PriceUtils.zeroAmount(cartLike.getCurrency())));
    }

    public CharSequence totalItemsCount(final CartLike<?> cartLike) {
        return cartLike.getLineItems().stream()
                    .mapToLong(LineItem::getQuantity)
                    .sum() + "";
    }

    public CharSequence enumToCamelCase(final Object enumValue) {
        return CtpEnumUtils.enumToCamelCase(enumValue.toString());
    }

    public CharSequence size(final List<?> list) {
        return list == null ? "0" : list.size() + "";
    }

    private boolean isNotAnyDefaultAddress(final Customer customer, final Address address) {
        final boolean isNotDefaultShipping = !Objects.equals(address.getId(), customer.getDefaultShippingAddressId());
        final boolean isNotDefaultBilling = !Objects.equals(address.getId(), customer.getDefaultBillingAddressId());
        return isNotDefaultShipping && isNotDefaultBilling;
    }

    public CharSequence withNonStandardAddresses(final Customer customer, final Options options) throws IOException {
        final List<Address> addresses = customer.getAddresses().stream()
                .filter(address -> isNotAnyDefaultAddress(customer, address))
                .collect(Collectors.toList());
        return options.fn(addresses);
    }

    public CharSequence withLimit(final List<?> list, final int limit, final Options options) throws IOException {
        final List<?> newList = ObjectUtils.firstNonNull(list, emptyList()).stream().limit(limit).collect(toList());
        return options.fn(newList);
    }

    public CharSequence withReversed(final List<?> list, final Options options) throws IOException {
        final List<?> reversed = new ArrayList<>(ObjectUtils.firstNonNull(list, emptyList()));
        Collections.reverse(reversed);
        return options.fn(reversed);
    }
}
