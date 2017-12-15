package com.commercetools.sunrise.framework.localization;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.projects.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.neovisionaries.i18n.CountryCode.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCountriesTest {

    private static final CountryCode DEFAULT_COUNTRY = CountryCode.getByLocale(Locale.getDefault());

    @Mock
    private Project project;

    @Test
    public void takesAvailableFromConfig() {
        mockProjectWithDACHCountries();
        final List<String> configCountries = asList("it", "fr");
        testAvailables(configCountries, countries -> assertThat(countries).containsExactly(IT, FR));
    }

    @Test
    public void takesAvailableFromProjectIfNotConfigured() {
        mockProjectWithDACHCountries();
        final List<String> configCountries = emptyList();
        testAvailables(configCountries, countries -> assertThat(countries).containsExactly(DE, AT, CH));
    }

    @Test
    public void takesAvailableFromSystemAsFallback() {
        mockProjectWithoutCountries();
        final List<String> configCountries = emptyList();
        testAvailables(configCountries, countries -> assertThat(countries).containsExactly(DEFAULT_COUNTRY));
    }

    @Test
    public void selectsPreferredCandidate() {
        final Countries countries = countriesWithAvailables(asList(CH, AT));
        assertThat(countries.preferred(asList(AT, IT))).isEqualTo(AT);
        assertThat(countries.preferred(asList(IT, AT))).isEqualTo(AT);
    }

    @Test
    public void selectsFirstAvailableIfNotMatchingCandidate() {
        final Countries countries = countriesWithAvailables(asList(CH, AT));
        assertThat(countries.preferred(singletonList(IT))).isEqualTo(CH);
    }

    @Test
    public void selectsSystemCountryAsFallback() {
        final Countries countries = countriesWithAvailables(emptyList());
        assertThat(countries.preferred(singletonList(IT))).isEqualTo(DEFAULT_COUNTRY);
    }

    private void mockProjectWithoutCountries() {
        when(project.getCountries()).thenReturn(emptyList());
    }

    private void mockProjectWithDACHCountries() {
        when(project.getCountries()).thenReturn(asList(DE, AT, CH));
    }

    private void testAvailables(final List<String> configCountries, final Consumer<List<CountryCode>> test) {
        final Configuration config = new Configuration(singletonMap("sunrise.localization.countries", configCountries));
        final Countries countries = new DefaultCountries(config, () -> project);
        test.accept(countries.availables());
    }

    private Countries countriesWithAvailables(final List<CountryCode> availables) {
        return new DefaultCountries(availables);
    }
}
