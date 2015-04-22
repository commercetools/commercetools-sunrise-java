package common.controllers;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryBuilder;
import io.sphere.sdk.models.LocalizedStrings;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class ByNameCategoryComparatorTest {

    @Test
    public void sortsAccordingToTheProvidedLocale() {
        List<Category> categories = asList(
                category(LocalizedStrings.of(GERMAN, "Hose", ENGLISH, "Pants")),
                category(LocalizedStrings.of(GERMAN, "Hemden", ENGLISH, "Shirts")),
                category(LocalizedStrings.of(GERMAN, "Kleider", ENGLISH, "Dresses"))
        );
        sort(categories, ENGLISH, (sortedNames) -> assertThat(sortedNames).containsExactly("Dresses", "Pants", "Shirts"));
    }

    @Test
    public void sortsWhenTheLocalizedNameForTheProvidedLocaleIsMissing() {
        List<Category> categories = asList(
                category(LocalizedStrings.of(GERMAN, "Hose", ENGLISH, "Pants")),
                category(LocalizedStrings.of(GERMAN, "Hemden")),
                category(LocalizedStrings.of(GERMAN, "Kleider", ENGLISH, "Dresses"))
        );
        sort(categories, ENGLISH, (sortedNames) -> assertThat(sortedNames).containsExactly("", "Dresses", "Pants"));
    }

    private void sort(List<Category> unsortedList, Locale locale, Consumer<List<String>> test) {
        final Stream<Category> sortedCategories = unsortedList.stream().sorted(new ByNameCategoryComparator(locale));
        final Stream<String> sortedNames = sortedCategories.map(category -> category.getName().get(locale).orElse(""));
        test.accept(sortedNames.collect(Collectors.toList()));
    }

    private static Category category(LocalizedStrings name) {
        final LocalizedStrings description = LocalizedStrings.ofEnglishLocale("");
        return CategoryBuilder.of("id", name, description).build();
    }
}
