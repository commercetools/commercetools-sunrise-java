package controllers;

import com.typesafe.config.ConfigFactory;
import exceptions.DefaultCountryNotFound;
import exceptions.InvalidCountryCode;
import org.junit.Test;
import play.Configuration;
import play.mvc.Http;

import java.util.*;

import static java.util.Collections.emptyMap;
import static controllers.CountryOperations.*;
import static com.neovisionaries.i18n.CountryCode.DE;
import static com.neovisionaries.i18n.CountryCode.AT;
import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Controller.session;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

public class CountryOperationsTest extends WithSunriseApplication {
    private CountryOperations operations;

    public void setUp(Configuration configuration) {
        operations = CountryOperations.of(configuration);
    }

    @Test
    public void userCountryIsCountryFoundInSession() {
        running(fakeApplication(), () -> {
            setUp(configWithAvailableCountries("AT"));
            setContext();
            session().put(COUNTRY_SESSION_KEY, "DE");
            assertThat(operations.country()).isEqualTo(DE);
        });
    }

    @Test
    public void userCountryIsDefaultWhenNoCountryFoundInSession() {
        running(fakeApplication(), () -> {
            setUp(configWithAvailableCountries("AT"));
            setContext();
            assertThat(operations.country()).isEqualTo(AT);
        });
    }

    @Test
    public void availableCountriesGetsAllCountriesFromConfiguration() {
        setUp(configWithAvailableCountries("DE", "AT"));
        assertThat(operations.availableCountries()).containsExactly(DE, AT);
    }

    @Test
    public void availableCountriesSkipsInvalidCountryFromConfiguration() {
        setUp(configWithAvailableCountries("INVALID", "AT"));
        assertThat(operations.availableCountries()).containsExactly(AT);
    }

    @Test
    public void defaultCountryIsFirstCountryFromConfiguration() {
        setUp(configWithAvailableCountries("DE", "AT"));
        assertThat(operations.defaultCountry()).isEqualTo(DE);
    }

    @Test
    public void defaultCountrySkipsInvalidCountryFromConfiguration() {
        setUp(configWithAvailableCountries("INVALID", "AT"));
        assertThat(operations.defaultCountry()).isEqualTo(AT);
    }

    @Test(expected = DefaultCountryNotFound.class)
    public void defaultCountryThrowsExceptionWhenNoneConfigured() {
        setUp(configWithAvailableCountries());
        operations.defaultCountry();
    }

    @Test
    public void changeCountrySavesCodeInSessionWhenValidCountryCode() {
        running(fakeApplication(), () -> {
            setUp(configWithAvailableCountries("DE"));
            setContext();
            operations.changeCountry("DE");
            assertThat(session().get(COUNTRY_SESSION_KEY)).isEqualTo("DE");
        });
    }

    @Test
    public void changeCountrySkipsCodeWhenCountryNotAvailable() {
        running(fakeApplication(), () -> {
            setUp(configWithAvailableCountries("DE"));
            setContext();
            operations.changeCountry("UK");
            assertThat(session().containsKey(COUNTRY_SESSION_KEY)).isFalse();
        });
    }

    @Test(expected = InvalidCountryCode.class)
    public void changeCountryThrowsExceptionWhenInvalidCountryCode() {
        setUp(configWithAvailableCountries());
        operations.changeCountry("INVALID");
    }

    @Test
    public void parserReturnsCountryWhenValidCountryCode() {
        assertThat(parseCode("DE").get()).isEqualTo(DE);
    }

    @Test
    public void parserReturnsAbsentWhenInvalidCountryCode() {
        assertThat(parseCode("INVALID").isPresent()).isFalse();
    }

    private Configuration configWithAvailableCountries(String...codes) {
        Map<String, List<String>> configMap = Collections.singletonMap(COUNTRY_CONFIG_LIST, Arrays.asList(codes));
        return new Configuration(ConfigFactory.parseMap(configMap));
    }

    private void setContext() {
        final Map<String, String> sessionData = new HashMap<>();
        final Http.Context context = new Http.Context(null, null, null, sessionData, emptyMap(), emptyMap());
        Http.Context.current.set(context);
    }
}
