package com.commercetools.sunrise.it;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryDraft;
import io.sphere.sdk.categories.CategoryDraftBuilder;
import io.sphere.sdk.categories.CategoryDraftDsl;
import io.sphere.sdk.categories.commands.CategoryCreateCommand;
import io.sphere.sdk.categories.commands.CategoryDeleteCommand;
import io.sphere.sdk.client.BlockingSphereClient;

import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.*;

public final class CategoryTestFixtures {

    private CategoryTestFixtures() {
    }

    public static void withCategory(final BlockingSphereClient client, final CategoryDraft categoryDraft, final Function<Category, Category> test) {
        final Category category = client.executeBlocking(CategoryCreateCommand.of(categoryDraft));
        final Category categoryAfterTest = test.apply(category);
        deleteCategoryWithRetry(client, categoryAfterTest);
    }

    public static CategoryDraftDsl categoryDraft() {
        return CategoryDraftBuilder.of(randomLocalizedKey(), randomLocalizedKey()).build();
    }

    public static void deleteCategoryWithRetry(final BlockingSphereClient client, final Category categoryAfterTest) {
        deleteWithRetry(client, categoryAfterTest, CategoryDeleteCommand::of, DEFAULT_DELETE_TTL);
    }
}
