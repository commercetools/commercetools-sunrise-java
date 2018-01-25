package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.SelectOption;
import com.commercetools.sunrise.models.carts.MyCart;
import com.commercetools.sunrise.models.carts.MyCartCreatorHook;
import com.commercetools.sunrise.models.carts.MyCartFetcherHook;
import com.commercetools.sunrise.models.carts.MyCartUpdaterHook;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Singleton
public final class CountrySelectorComponent implements ControllerComponent, PageDataHook, MyCartCreatorHook, MyCartFetcherHook, MyCartUpdaterHook {

    private final Provider<CountryCode> countryProvider;
    private final Provider<Locale> localeProvider;
    private final Countries countries;
    private final CountryInSession countryInSession;
    private final MyCart myCart;

    @Inject
    CountrySelectorComponent(final Provider<CountryCode> countryProvider, final Provider<Locale> localeProvider,
                             final Countries countries, final CountryInSession countryInSession, final MyCart myCart) {
        this.countryProvider = countryProvider;
        this.localeProvider = localeProvider;
        this.countries = countries;
        this.countryInSession = countryInSession;
        this.myCart = myCart;
    }

    @Override
    public CompletionStage<Cart> on(final CartCreateCommand request, final Function<CartCreateCommand, CompletionStage<Cart>> nextComponent) {
        final CountryCode country = countryProvider.get();
        final CartDraft draft = CartDraftBuilder.of(request.getDraft())
                .country(country)
                .shippingAddress(Address.of(country))
                .build();
        return nextComponent.apply(request.withDraft(draft));
    }

    @Override
    public CompletionStage<Optional<Cart>> on(final CartQuery request, final Function<CartQuery, CompletionStage<Optional<Cart>>> nextComponent) {
        final CompletionStage<Optional<Cart>> cartOptStage = nextComponent.apply(request);
        cartOptStage.thenAcceptAsync(cartOpt -> cartOpt
                .ifPresent(cart -> Optional.ofNullable(cart.getCountry())
                        .ifPresent(this::useCartCountryAsUserCountry)), HttpExecution.defaultContext());
        return cartOptStage;
    }

    private void useCartCountryAsUserCountry(final CountryCode cartCountry) {
        final boolean haveSameCountry = countryInSession.findCountry()
                .map(userCountry -> userCountry.equals(cartCountry))
                .orElse(false);
        if (!haveSameCountry) countryInSession.store(cartCountry);
    }

    @Override
    public CompletionStage<Cart> on(final CartUpdateCommand request, final Function<CartUpdateCommand, CompletionStage<Cart>> nextComponent) {
        final CountryCode userCountry = countryProvider.get();
        return myCart.get()
                .thenApply(cartOpt -> cartOpt
                        .map(cart -> buildUpdateActionsToSetCountry(cart, userCountry))
                        .filter(updateActions -> !updateActions.isEmpty())
                        .map(request::plusUpdateActions)
                        .orElse(request))
                .thenComposeAsync(nextComponent, HttpExecution.defaultContext());
    }

    private List<UpdateAction<Cart>> buildUpdateActionsToSetCountry(final Cart cart, final CountryCode userCountry) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        final boolean haveSameCountry = cart.getShippingAddress() != null
                && cart.getShippingAddress().getCountry().equals(userCountry);
        if (!haveSameCountry) {
            final Address shippingAddress = Optional.ofNullable(cart.getShippingAddress())
                    .map(address -> address.withCountry(userCountry))
                    .orElseGet(() -> Address.of(userCountry));
            updateActions.add(SetShippingAddress.of(shippingAddress));
            updateActions.add(SetCountry.of(userCountry));
        }
        return updateActions;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return completedFuture(pageData.put("countryOptions", options()));
    }

    private List<SelectOption> options() {
        final CountryCode selectedCountry = countryProvider.get();
        final Locale locale = localeProvider.get();
        return countries.availables().stream()
                .map(country -> createOption(country, selectedCountry, locale))
                .collect(toList());
    }

    private SelectOption createOption(final CountryCode country, final CountryCode selectedCountry, final Locale locale) {
        final SelectOption option = new SelectOption();
        option.setLabel(country.toLocale().getDisplayCountry(locale));
        option.setValue(country.getAlpha2());
        option.setSelected(country.equals(selectedCountry));
        return option;
    }
}
