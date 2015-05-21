package common.countries;

import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import play.Configuration;
import play.mvc.Http;

import java.util.*;
import java.util.function.Consumer;

import static com.neovisionaries.i18n.CountryCode.AT;
import static com.neovisionaries.i18n.CountryCode.DE;
import static common.countries.CountryOperations.*;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static play.mvc.Controller.session;

public class CountryOperationsTest {

    @Test
    public void userCountryIsCountryFoundInSession() {
        testWith(availableCountries("AT"), operations -> {
            session().put(COUNTRY_SESSION_KEY, "DE");
            assertThat(operations.country()).isEqualTo(DE);
        });
    }

    @Test
    public void userCountryIsDefaultWhenNoCountryFoundInSession() {
        testWith(availableCountries("AT"), ops -> assertThat(ops.country()).isEqualTo(AT));
    }

    @Test
    public void availableCountriesGetsAllCountriesFromConfiguration() {
        testWith(availableCountries("DE", "AT"), ops -> assertThat(ops.availableCountries()).containsExactly(DE, AT));
    }

    @Test
    public void availableCountriesSkipsInvalidCountryFromConfiguration() {
        testWith(availableCountries("INVALID", "AT"), ops -> assertThat(ops.availableCountries()).containsExactly(AT));
    }

    @Test
    public void defaultCountryIsFirstCountryFromConfiguration() {
        testWith(availableCountries("DE", "AT"), ops -> assertThat(ops.defaultCountry()).isEqualTo(DE));
    }

    @Test
    public void defaultCountrySkipsInvalidCountryFromConfiguration() {
        testWith(availableCountries("INVALID", "AT"), ops -> assertThat(ops.defaultCountry()).isEqualTo(AT));
    }

    @Test
    public void defaultCountryThrowsExceptionWhenNoneConfigured() {
        testWith(availableCountries(), ops ->
                assertThatThrownBy(ops::defaultCountry).isInstanceOf(DefaultCountryNotFound.class));
    }

    @Test
    public void changeCountrySavesCodeInSessionWhenValidCountryCode() {
        testWith(availableCountries("DE"), ops -> {
            ops.changeCountry("DE");
            assertThat(session().get(COUNTRY_SESSION_KEY)).isEqualTo("DE");
        });
    }

    @Test
    public void changeCountrySkipsCodeWhenCountryNotAvailable() {
        testWith(availableCountries("DE"), ops -> {
            ops.changeCountry("UK");
            assertThat(session().containsKey(COUNTRY_SESSION_KEY)).isFalse();
        });
    }

    @Test
    public void changeCountryThrowsExceptionWhenInvalidCountryCode() {
        testWith(availableCountries("DE"), ops ->
                assertThatThrownBy(() -> ops.changeCountry("INVALID")).isInstanceOf(InvalidCountryCode.class));
    }

    @Test
    public void parserReturnsCountryWhenValidCountryCode() {
        assertThat(parseCode("DE").get()).isEqualTo(DE);
    }

    @Test
    public void parserReturnsAbsentWhenInvalidCountryCode() {
        assertThat(parseCode("INVALID").isPresent()).isFalse();
    }

    private void testWith(List<String> availableCountries, Consumer<CountryOperations> test) {
        Http.Context.current.set(emptyContext());
        Configuration configuration = configWithAvailableCountries(availableCountries);
        CountryOperations operations = CountryOperations.of(configuration);
        test.accept(operations);
        Http.Context.current.remove();
    }

    private List<String> availableCountries(String ... countryCodes) {
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
