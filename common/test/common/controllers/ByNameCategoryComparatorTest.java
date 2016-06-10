package common.controllers;

import com.commercetools.sunrise.common.controllers.ByNameCategoryComparator;
import io.sphere.sdk.categories.Category;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
        List<Category> categories = asList(category("Pants", "Hose"), category("Shirts", "Hemden"), category("Dresses", "Kleider"));
        sort(categories, ENGLISH, (sortedNames) -> assertThat(sortedNames).containsExactly("Dresses", "Pants", "Shirts"));
    }

    @Test
    public void sortsWhenTheLocalizedNameForTheProvidedLocaleIsMissing() {
        List<Category> categories = asList(category("Pants", "Hose"), category(null, "Hemden"), category("Dresses", "Kleider"));
        sort(categories, ENGLISH, (sortedNames) -> assertThat(sortedNames).containsExactly(null, "Dresses", "Pants"));
    }

    private void sort(List<Category> unsortedList, Locale locale, Consumer<List<String>> test) {
        final Stream<Category> sortedCategories = unsortedList.stream().sorted(new ByNameCategoryComparator(locale));
        final Stream<String> sortedNames = sortedCategories.map(category -> category.getName().get(locale));
        test.accept(sortedNames.collect(Collectors.toList()));
    }

    private static Category category(@Nullable final String englishName, final String germanName) {
        final String englishNameJson = Optional.ofNullable(englishName).map(e -> "\"en\": \""+ e +"\",").orElse("");
        final String json =
                "{" +
                "  \"name\": {" +
                    englishNameJson +
                "    \"de\" : \""+ germanName + "\"" +
                "  }" +
                "}";
        return readObject(json, Category.typeReference());
    }
}
