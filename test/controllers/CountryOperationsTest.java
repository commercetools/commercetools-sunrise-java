package controllers;

import com.typesafe.config.ConfigFactory;
import exceptions.DefaultCountryNotFound;
import exceptions.InvalidCountryCode;
import org.junit.BeforeClass;
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

public class CountryOperationsTest extends WithSunriseApplication {
    private CountryOperations operations;

    @BeforeClass
    public static void setUp() {
        final Map<String, String> sessionData = new HashMap<>();
        Http.Context context = new Http.Context(null, null, null, sessionData, emptyMap(), emptyMap());
        Http.Context.current.set(context);
    }

    public void init(List<String> availableCountries, Runnable test) {
        Configuration configuration = configWithAvailableCountries(availableCountries);
        operations = CountryOperations.of(configuration);
        test.run();
        session().clear();
    }

    @Test
    public void userCountryIsCountryFoundInSession() {
        init(withAvailableCountries("AT"), () -> {
            session().put(COUNTRY_SESSION_KEY, "DE");
            assertThat(operations.country()).isEqualTo(DE);
        });
    }

    @Test
    public void userCountryIsDefaultWhenNoCountryFoundInSession() {
        init(withAvailableCountries("AT"), () ->
            assertThat(operations.country()).isEqualTo(AT)
        );
    }

    @Test
    public void availableCountriesGetsAllCountriesFromConfiguration() {
        init(withAvailableCountries("DE", "AT"), () ->
            assertThat(operations.availableCountries()).containsExactly(DE, AT)
        );
    }

    @Test
    public void availableCountriesSkipsInvalidCountryFromConfiguration() {
        init(withAvailableCountries("INVALID", "AT"), () ->
            assertThat(operations.availableCountries()).containsExactly(AT)
        );
    }

    @Test
    public void defaultCountryIsFirstCountryFromConfiguration() {
        init(withAvailableCountries("DE", "AT"), () ->
            assertThat(operations.defaultCountry()).isEqualTo(DE));
    }

    @Test
    public void defaultCountrySkipsInvalidCountryFromConfiguration() {
        init(withAvailableCountries("INVALID", "AT"), () ->
            assertThat(operations.defaultCountry()).isEqualTo(AT));
    }

    @Test(expected = DefaultCountryNotFound.class)
    public void defaultCountryThrowsExceptionWhenNoneConfigured() {
        init(withAvailableCountries(), () ->
            operations.defaultCountry());
    }

    @Test
    public void changeCountrySavesCodeInSessionWhenValidCountryCode() {
        init(withAvailableCountries("DE"), () -> {
            operations.changeCountry("DE");
            assertThat(session().get(COUNTRY_SESSION_KEY)).isEqualTo("DE");
        });
    }

    @Test
    public void changeCountrySkipsCodeWhenCountryNotAvailable() {
        init(withAvailableCountries("DE"), () -> {
            operations.changeCountry("UK");
            assertThat(session().containsKey(COUNTRY_SESSION_KEY)).isFalse();
        });
    }

    @Test(expected = InvalidCountryCode.class)
    public void changeCountryThrowsExceptionWhenInvalidCountryCode() {
        init(withAvailableCountries(), () ->
            operations.changeCountry("INVALID"));
    }

    @Test
    public void parserReturnsCountryWhenValidCountryCode() {
        assertThat(parseCode("DE").get()).isEqualTo(DE);
    }

    @Test
    public void parserReturnsAbsentWhenInvalidCountryCode() {
        assertThat(parseCode("INVALID").isPresent()).isFalse();
    }

    private List<String> withAvailableCountries(String...countryCodes) {
        return Arrays.asList(countryCodes);
    }

    private Configuration configWithAvailableCountries(List<String> availableCountries) {
        Map<String, List<String>> configMap = Collections.singletonMap(COUNTRY_CONFIG_LIST, availableCountries);
        return new Configuration(ConfigFactory.parseMap(configMap));
    }
}
