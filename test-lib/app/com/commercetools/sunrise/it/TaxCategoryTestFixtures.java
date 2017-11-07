package com.commercetools.sunrise.it;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.models.ResourceIdentifiable;
import io.sphere.sdk.products.ProductDraftBuilder;
import io.sphere.sdk.products.ProductVariantDraft;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.taxcategories.TaxCategory;
import io.sphere.sdk.taxcategories.TaxCategoryDraft;
import io.sphere.sdk.taxcategories.commands.TaxCategoryCreateCommand;
import io.sphere.sdk.taxcategories.commands.TaxCategoryDeleteCommand;

import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.*;

public final class TaxCategoryTestFixtures {

    private TaxCategoryTestFixtures() {
    }

    public static void withTaxCategory(final BlockingSphereClient client, final TaxCategoryDraft taxCategoryDraft, final Function<TaxCategory, TaxCategory> test) {
        final TaxCategory taxCategory = client.executeBlocking(TaxCategoryCreateCommand.of(taxCategoryDraft));
        final TaxCategory taxCategoryAfterTest = test.apply(taxCategory);
        deleteTaxCategoryWithRetry(client, taxCategoryAfterTest);
    }

    public static ProductDraftBuilder productDraft(final ResourceIdentifiable<ProductType> productType, final ProductVariantDraft productVariantDraft) {
        return ProductDraftBuilder.of(productType, randomLocalizedKey(), randomLocalizedKey(), productVariantDraft);
    }

    public static void deleteTaxCategoryWithRetry(final BlockingSphereClient client, final TaxCategory taxCategory) {
        deleteWithRetry(client, taxCategory, TaxCategoryDeleteCommand::of, DEFAULT_DELETE_TTL);
    }
}
