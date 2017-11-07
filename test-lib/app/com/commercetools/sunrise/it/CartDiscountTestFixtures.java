package com.commercetools.sunrise.it;

import io.sphere.sdk.cartdiscounts.CartDiscount;
import io.sphere.sdk.cartdiscounts.CartDiscountDraft;
import io.sphere.sdk.cartdiscounts.commands.CartDiscountCreateCommand;
import io.sphere.sdk.cartdiscounts.commands.CartDiscountDeleteCommand;
import io.sphere.sdk.cartdiscounts.queries.CartDiscountQuery;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.discountcodes.DiscountCode;
import io.sphere.sdk.discountcodes.queries.DiscountCodeQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.it.DiscountCodeTestFixtures.deleteDiscountCodeWithRetry;
import static com.commercetools.sunrise.it.TestFixtures.DEFAULT_DELETE_TTL;
import static com.commercetools.sunrise.it.TestFixtures.deleteWithRetry;

public final class CartDiscountTestFixtures {

    private CartDiscountTestFixtures() {
    }

    public static void withCartDiscount(final BlockingSphereClient client, final CartDiscountDraft cartDiscountDraft, final Function<CartDiscount, CartDiscount> test) {
        final CartDiscount cartDiscount = client.executeBlocking(CartDiscountCreateCommand.of(cartDiscountDraft));
        final CartDiscount cartDiscountAfterTest = test.apply(cartDiscount);
        deleteCartDiscountWithRetry(client, cartDiscountAfterTest);
    }

    public static void deleteCartDiscountWithRetry(final BlockingSphereClient client, final CartDiscount cartDiscount) {
        deleteWithRetry(client, cartDiscount, CartDiscountDeleteCommand::of, DEFAULT_DELETE_TTL);
    }

    public static void deleteCartDiscountAndDiscountCodes(final BlockingSphereClient client, final String englishCartDiscountName, final String cartDiscountSortOrder) {
        final CartDiscountQuery cartDiscountQuery = CartDiscountQuery.of()
                .withPredicates(cartDiscountQueryModel -> cartDiscountQueryModel.name().lang(Locale.ENGLISH).is(englishCartDiscountName));
        final PagedQueryResult<CartDiscount> cartDiscountQueryResult = client.executeBlocking(cartDiscountQuery);
        final List<CartDiscount> cartDiscountsToDelete = cartDiscountQueryResult.getResults().stream()
                .filter(cartDiscount -> cartDiscount.getSortOrder().equals(cartDiscountSortOrder))
                .collect(Collectors.toList());
        final Set<String> cartDiscountIds = cartDiscountsToDelete.stream()
                .map(CartDiscount::getId)
                .collect(Collectors.toSet());
        final PagedQueryResult<DiscountCode> discountCodeQueryResult = client.executeBlocking(DiscountCodeQuery.of());
        final List<DiscountCode> discountCodesToDelete = discountCodeQueryResult.getResults().stream()
                .filter(discountCode -> discountCode.getCartDiscounts().stream().filter(ref -> cartDiscountIds.contains(ref.getId())).findAny().isPresent())
                .collect(Collectors.toList());
        discountCodesToDelete.forEach(discountCode -> deleteDiscountCodeWithRetry(client, discountCode));
        cartDiscountsToDelete.forEach(cartDiscount -> deleteCartDiscountWithRetry(client, cartDiscount));
    }
}
