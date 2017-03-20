package com.commercetools.sunrise.common.categorytree;

import com.commercetools.sunrise.common.categorytree.ByNameCategoryComparator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.LocalizedString;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ByNameCategoryComparatorTest {

    @Test
    public void sortsAccordingToTheProvidedLocale() {
        final Category pants = categoryWithName(LocalizedString.of(ENGLISH, "Pants", GERMAN, "Hose"));
        final Category shirts = categoryWithName(LocalizedString.of(ENGLISH, "Shirts", GERMAN, "Hemden"));
        final Category dresses = categoryWithName(LocalizedString.of(ENGLISH, "Dresses", GERMAN, "Kleider"));
        final List<Category> list = asList(pants, shirts, dresses);
        list.sort(new ByNameCategoryComparator(ENGLISH));
        assertThat(list)
                .extracting(category -> category.getName().get(ENGLISH))
                .containsExactly("Dresses", "Pants", "Shirts");
    }

    @Test
    public void sortsWhenTheLocalizedNameForTheProvidedLocaleIsMissing() {
        final Category pants = categoryWithName(LocalizedString.of(ENGLISH, "Pants", GERMAN, "Hose"));
        final Category shirts = categoryWithName(LocalizedString.of(GERMAN, "Hemden"));
        final Category dresses = categoryWithName(LocalizedString.of(ENGLISH, "Dresses", GERMAN, "Kleider"));
        final List<Category> list = asList(pants, shirts, dresses);
        list.sort(new ByNameCategoryComparator(ENGLISH));
        assertThat(list)
                .extracting(category -> category.getName().get(ENGLISH))
                .containsExactly(null, "Dresses", "Pants");
    }

    private Category categoryWithName(final LocalizedString name) {
        final Category category = mock(Category.class);
        when(category.getName()).thenReturn(name);
        return category;
    }
}
