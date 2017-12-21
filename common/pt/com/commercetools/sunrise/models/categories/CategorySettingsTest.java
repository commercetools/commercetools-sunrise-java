package com.commercetools.sunrise.models.categories;

import org.junit.Test;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySettingsTest extends WithApplication {

    @Test
    public void fallbacksToDefaultValues() throws Exception {
        final CategorySettings categorySettings = app.injector().instanceOf(CategorySettings.class);
        assertThat(categorySettings.cacheExpiration())
                .as("Cache expiration")
                .isEmpty();
        assertThat(categorySettings.cacheKey())
                .as("Cache key")
                .isNotNull();
        assertThat(categorySettings.discardEmpty())
                .as("Discard empty")
                .isFalse();
        assertThat(categorySettings.sortExpressions())
                .as("Sort expressions")
                .isNotEmpty();
        assertThat(categorySettings.navigationExternalId())
                .as("Navigation external ID")
                .isEmpty();
        assertThat(categorySettings.newExtId())
                .as("New external ID")
                .isEmpty();
        assertThat(categorySettings.specialCategories())
                .as("Special categories")
                .isEmpty();
    }
}