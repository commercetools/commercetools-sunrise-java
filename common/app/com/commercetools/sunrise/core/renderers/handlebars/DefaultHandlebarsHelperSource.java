package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sdk.CtpEnumUtils;
import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.reverserouters.myaccount.addressbook.AddressBookReverseRouter;
import com.commercetools.sunrise.core.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.models.carts.CartPriceUtils;
import com.commercetools.sunrise.models.products.ProductPriceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.products.PriceUtils;
import io.sphere.sdk.products.ProductVariant;
import lombok.NonNull;
import play.mvc.Call;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.money.MonetaryAmount;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.core.renderers.handlebars.HandlebarsTemplateEngine.CMS_PAGE_IN_CONTEXT_KEY;
import static java.util.stream.Collectors.toList;

@Singleton
public class DefaultHandlebarsHelperSource implements HandlebarsHelperSource {

    private final I18nResolver i18nResolver;
    private final ProductReverseRouter productReverseRouter;
    private final PriceFormatter priceFormatter;
    private final DateTimeFormatter dateTimeFormatter;
    private final MyOrdersReverseRouter myOrdersReverseRouter;
    private final AddressBookReverseRouter addressBookReverseRouter;

    @Inject
    protected DefaultHandlebarsHelperSource(final I18nResolver i18nResolver, ProductReverseRouter productReverseRouter, PriceFormatter priceFormatter, DateTimeFormatter dateTimeFormatter, MyOrdersReverseRouter myOrdersReverseRouter, AddressBookReverseRouter addressBookReverseRouter) {
        this.i18nResolver = i18nResolver;
        this.productReverseRouter = productReverseRouter;
        this.priceFormatter = priceFormatter;
        this.dateTimeFormatter = dateTimeFormatter;
        this.myOrdersReverseRouter = myOrdersReverseRouter;
        this.addressBookReverseRouter = addressBookReverseRouter;
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
        final Optional<CmsPage> cmsPage = getCmsPageFromContext(options.context);
        return cmsPage.flatMap(page -> page.field(fieldName)).orElse("");
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

    private static Optional<CmsPage> getCmsPageFromContext(final Context context) {
        final Object cmsPageAsObject = context.get(CMS_PAGE_IN_CONTEXT_KEY);
        if (cmsPageAsObject instanceof CmsPage) {
            return Optional.of((CmsPage) cmsPageAsObject);
        } else {
            return Optional.empty();
        }
    }

    @Nullable
    public CharSequence imageUrl(@NonNull final ProductVariant object) {
        return object.getImages().stream().map(image -> (CharSequence) image.getUrl()).findFirst().orElse(null);
    }

    public CharSequence variantUrlFromLineItem(final Object object) {
        if (object instanceof LineItem) {
            return productReverseRouter.productDetailPageCall((LineItem)object).map(Call::url).orElse("");
        } else if (object instanceof io.sphere.sdk.shoppinglists.LineItem ) {
            return productReverseRouter.productDetailPageCall((io.sphere.sdk.shoppinglists.LineItem )object).map(Call::url).orElse("");
        } else {
            throw new IllegalArgumentException("is not a LineItem " + object);
        }
    }

    public CharSequence currentPriceForLineItem(final LineItem object) {
        return formatPrice(ProductPriceUtils.calculateAppliedProductPrice(object));
    }

    public CharSequence oldPriceForLineItem(final LineItem object) {
        return ProductPriceUtils.calculatePreviousProductPrice(object)
                .map(oldPrice -> formatPrice(oldPrice)).orElse("");
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

    public CharSequence formatDateTime(final ZonedDateTime time) {
        return dateTimeFormatter.format(time);
    }

    public CharSequence enumToCamelCase(final Object enumValue) {
        return CtpEnumUtils.enumToCamelCase(enumValue.toString());
    }

    public CharSequence myOrderDetailPageUrl(final Order order) {
        return myOrdersReverseRouter
                .myOrderDetailPageCall(order)
                .map(Call::url)
                .orElse("");
    }

    public CharSequence size(final List<?> list) {
        return list == null ? "0" : list.size() + "";
    }

    public CharSequence editAddressUrl(final Address address) {
        return addressBookReverseRouter.changeAddressPageCall(address).map(Call::url).orElse("");
    }

    public CharSequence deleteAddressUrl(final Address address) {
        return addressBookReverseRouter.removeAddressProcessCall(address).map(Call::url).orElse("");
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
}
