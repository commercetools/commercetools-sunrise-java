package common.controllers;

import io.sphere.sdk.categories.Category;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.sphere.sdk.json.SphereJsonUtils.readObject;
import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class ByNameCategoryComparatorTest {

    @Test
    public void sortsAccordingToTheProvidedLocale() {
        List<Category> categories = asList(category(PANTS), category(SHIRTS), category(DRESSES));
        sort(categories, ENGLISH, (sortedNames) -> assertThat(sortedNames).containsExactly("Dresses", "Pants", "Shirts"));
    }

    @Test
    public void sortsWhenTheLocalizedNameForTheProvidedLocaleIsMissing() {
        List<Category> categories = asList(category(PANTS), category(SHIRTS_WITHOUT_ENGLISH), category(DRESSES));
        sort(categories, ENGLISH, (sortedNames) -> assertThat(sortedNames).containsExactly(null, "Dresses", "Pants"));
    }

    private void sort(List<Category> unsortedList, Locale locale, Consumer<List<String>> test) {
        final Stream<Category> sortedCategories = unsortedList.stream().sorted(new ByNameCategoryComparator(locale));
        final Stream<String> sortedNames = sortedCategories.map(category -> category.getName().get(locale));
        test.accept(sortedNames.collect(Collectors.toList()));
    }

    private static Category category(final String jsonAsString) {
        return readObject(jsonAsString, Category.typeReference());
    }

    private static final String PANTS =
            "{" +
            "  \"name\": {" +
            "    \"en\": \"Pants\"," +
            "    \"de\" : \"Hose\"" +
            "  }" +
            "}";

    private static final String SHIRTS =
            "{" +
            "  \"name\": {" +
            "    \"en\": \"Shirts\"," +
            "    \"de\" : \"Hemden\"" +
            "  }" +
            "}";

    private static final String DRESSES =
            "{" +
            "  \"name\": {" +
            "    \"en\": \"Dresses\"," +
            "    \"de\" : \"Kleider\"" +
            "  }" +
            "}";

    private static final String SHIRTS_WITHOUT_ENGLISH =
            "{" +
            "  \"name\": {" +
            "    \"de\" : \"Hemden\"" +
            "  }" +
            "}";
}
