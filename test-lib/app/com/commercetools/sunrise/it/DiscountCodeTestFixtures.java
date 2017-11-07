package com.commercetools.sunrise.it;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.discountcodes.DiscountCode;
import io.sphere.sdk.discountcodes.DiscountCodeDraft;
import io.sphere.sdk.discountcodes.commands.DiscountCodeCreateCommand;
import io.sphere.sdk.discountcodes.commands.DiscountCodeDeleteCommand;

import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.DEFAULT_DELETE_TTL;
import static com.commercetools.sunrise.it.TestFixtures.deleteWithRetry;

public final class DiscountCodeTestFixtures {

    private DiscountCodeTestFixtures() {
    }

    public static void withDiscountCode(final BlockingSphereClient client, final DiscountCodeDraft discountCodeDraft, final Function<DiscountCode, DiscountCode> test) {
        final DiscountCode discountCode = client.executeBlocking(DiscountCodeCreateCommand.of(discountCodeDraft));
        final DiscountCode discountCodeAfterTest = test.apply(discountCode);
        deleteDiscountCodeWithRetry(client, discountCodeAfterTest);
    }

    public static void deleteDiscountCodeWithRetry(final BlockingSphereClient client, final DiscountCode discountCode) {
        deleteWithRetry(client, discountCode, DiscountCodeDeleteCommand::of, DEFAULT_DELETE_TTL);
    }
}
