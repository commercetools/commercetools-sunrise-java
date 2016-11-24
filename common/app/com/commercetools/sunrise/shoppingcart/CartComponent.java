package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.actions.CartLoadedActionHook;
import com.commercetools.sunrise.hooks.events.CartCreatedHook;
import com.commercetools.sunrise.hooks.events.CartLoadedHook;
import com.commercetools.sunrise.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.hooks.requests.CartCreateCommandHook;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import com.google.inject.Injector;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.CartDraftDsl;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public class CartComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CartLoadedHook, CartUpdatedHook, CartCreatedHook, CartCreateCommandHook, CartLoadedActionHook {

    @Inject
    private Injector injector;

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        return Optional.ofNullable(customerSignInResult.getCart())
                .map(cart -> updateCartWithMissingInfoOnSignIn(cart, customerSignInResult.getCustomer()))
                .orElseGet(() -> completedFuture(null))
                .thenAcceptAsync(this::overwriteCartInSession, HttpExecution.defaultContext());
    }

    @Override
    public CompletionStage<?> onCartLoaded(final Cart cart) {
        overwriteCartInSession(cart);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCartUpdated(final Cart cart) {
        overwriteCartInSession(cart);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCartCreated(final Cart cart) {
        overwriteCartInSession(cart);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<Cart> onCartLoadedAction(final Cart cart, final ExpansionPathContainer<Cart> expansionPathContainer) {
        final CountryCode country = injector.getInstance(UserContext.class).country();
        final boolean hasDifferentCountry = !country.equals(cart.getCountry());
        return hasDifferentCountry ? updateCartCountry(cart, country, expansionPathContainer) : completedFuture(cart);
    }

    @Override
    public CartCreateCommand onCartCreateCommand(final CartCreateCommand cartCreateCommand) {
        final CartDraft cartDraft = cartDraftWithAdditionalInfo(cartCreateCommand.getDraft());
        return CartCreateCommand.of(cartDraft)
                .withExpansionPaths(cartCreateCommand.expansionPaths());
    }

    CompletionStage<Cart> updateCartWithMissingInfoOnSignIn(final Cart cart, final Customer customer) {
        final List<UpdateAction<Cart>> updateActions = actionsToUpdateCartOnSignIn(cart, customer);
        if (!updateActions.isEmpty()) {
            final CartUpdateCommand command = CartUpdateCommand.of(cart, updateActions);
            return injector.getInstance(SphereClient.class).execute(command);
        } else {
            return completedFuture(cart);
        }
    }

    private List<UpdateAction<Cart>> actionsToUpdateCartOnSignIn(final Cart cart, final Customer customer) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        if (cart.getCustomerEmail() == null) {
            updateActions.add(SetCustomerEmail.of(customer.getEmail()));
        }
        return updateActions;
    }

    private CartDraftDsl cartDraftWithAdditionalInfo(final CartDraft cartDraft) {
        final CountryCode country = injector.getInstance(UserContext.class).country();
        final CustomerInSession customerInSession = injector.getInstance(CustomerInSession.class);
        return CartDraftBuilder.of(cartDraft)
                .country(country)
                .shippingAddress(Address.of(country))
                .customerId(customerInSession.findCustomerId().orElse(null))
                .customerEmail(customerInSession.findCustomerEmail().orElse(null))
                .build();
    }

    private void overwriteCartInSession(final Cart cart) {
        injector.getInstance(CartInSession.class).store(cart);
    }

    /**
     * Updates the country of the cart, both {@code country} and {@code shippingAddress} country fields.
     * This is necessary in order to obtain prices with tax calculation.
     * @param cart the cart which country needs to be updated
     * @param country the country to set in the cart
     * @param expansionPathContainer expansion paths to be honored
     * @return the completionStage of a cart with the given country
     */
    private CompletionStage<Cart> updateCartCountry(final Cart cart, final CountryCode country,
                                                    final ExpansionPathContainer<Cart> expansionPathContainer) {
        // TODO Handle case where some line items do not exist for this country
        final Address shippingAddress = Optional.ofNullable(cart.getShippingAddress())
                .map(address -> address.withCountry(country))
                .orElseGet(() -> Address.of(country));
        final CartUpdateCommand updateCommand = CartUpdateCommand.of(cart,
                asList(SetShippingAddress.of(shippingAddress), SetCountry.of(country)))
                .withExpansionPaths(expansionPathContainer);
        return injector.getInstance(SphereClient.class).execute(updateCommand);
    }
}
