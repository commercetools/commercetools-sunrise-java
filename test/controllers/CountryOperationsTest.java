package controllers;

import com.typesafe.config.ConfigFactory;
import exceptions.DefaultCountryNotFound;
import exceptions.InvalidCountryCode;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Configuration;
import play.mvc.Http;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static controllers.CountryOperations.*;
import static com.neovisionaries.i18n.CountryCode.DE;
import static com.neovisionaries.i18n.CountryCode.AT;
import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Controller.session;

public class CountryOperationsTest extends WithSunriseApplication {

    public void init(List<String> availableCountries, Consumer<CountryOperations> test) {
        Http.Context.current.set(emptyContext());
        Configuration configuration = configWithAvailableCountries(availableCountries);
        CountryOperations operations = CountryOperations.of(configuration);
        test.accept(operations);
        Http.Context.current.remove();
    }

    @Test
    public void userCountryIsCountryFoundInSession() {
        init(availableCountries("AT"), operations -> {
            session().put(COUNTRY_SESSION_KEY, "DE");
            assertThat(operations.country()).isEqualTo(DE);
        });
    }

    @Test
    public void userCountryIsDefaultWhenNoCountryFoundInSession() {
        init(availableCountries("AT"), ops -> assertThat(ops.country()).isEqualTo(AT));
    }

    @Test
    public void availableCountriesGetsAllCountriesFromConfiguration() {
        init(availableCountries("DE", "AT"), ops -> assertThat(ops.availableCountries()).containsExactly(DE, AT));
    }

    @Test
    public void availableCountriesSkipsInvalidCountryFromConfiguration() {
        init(availableCountries("INVALID", "AT"), ops -> assertThat(ops.availableCountries()).containsExactly(AT));
    }

    @Test
    public void defaultCountryIsFirstCountryFromConfiguration() {
        init(availableCountries("DE", "AT"), ops -> assertThat(ops.defaultCountry()).isEqualTo(DE));
    }

    @Test
    public void defaultCountrySkipsInvalidCountryFromConfiguration() {
        init(availableCountries("INVALID", "AT"), ops -> assertThat(ops.defaultCountry()).isEqualTo(AT));
    }

    @Test(expected = DefaultCountryNotFound.class)
    public void defaultCountryThrowsExceptionWhenNoneConfigured() {
        init(availableCountries(), ops -> ops.defaultCountry());
    }

    @Test
    public void changeCountrySavesCodeInSessionWhenValidCountryCode() {
        init(availableCountries("DE"), ops -> {
            ops.changeCountry("DE");
            assertThat(session().get(COUNTRY_SESSION_KEY)).isEqualTo("DE");
        });
    }

    @Test
    public void changeCountrySkipsCodeWhenCountryNotAvailable() {
        init(availableCountries("DE"), ops -> {
            ops.changeCountry("UK");
            assertThat(session().containsKey(COUNTRY_SESSION_KEY)).isFalse();
        });
    }

    @Test(expected = InvalidCountryCode.class)
    public void changeCountryThrowsExceptionWhenInvalidCountryCode() {
        init(availableCountries(), ops -> ops.changeCountry("INVALID"));
    }

    @Test
    public void parserReturnsCountryWhenValidCountryCode() {
        assertThat(parseCode("DE").get()).isEqualTo(DE);
    }

    @Test
    public void parserReturnsAbsentWhenInvalidCountryCode() {
        assertThat(parseCode("INVALID").isPresent()).isFalse();
    }

    private List<String> availableCountries(String...countryCodes) {
        return Arrays.asList(countryCodes);
    }

    private Http.Context emptyContext() {
        return new Http.Context(null, null, null, new HashMap<>(), emptyMap(), emptyMap());
    }

    private Configuration configWithAvailableCountries(List<String> availableCountries) {
        Map<String, List<String>> configMap = Collections.singletonMap(COUNTRY_CONFIG_LIST, availableCountries);
        return new Configuration(ConfigFactory.parseMap(configMap));
    }
}
