package controllers;

import com.neovisionaries.i18n.CountryCode;
import com.typesafe.config.ConfigFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.Configuration;
import play.mvc.Http;
import testUtils.TestableRequestBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class SunriseControllerTest extends WithSunriseApplication {

    private SunriseController controller;

    @Before
    public void setUp() {
        controller = new SunriseController(null, null);
    }

    @Test
    public void userCountryIsCountryFoundInCookie() {
        List<Http.Cookie> cookies = Collections.singletonList(countryCookie("SHOP_COUNTRY", CountryCode.ES));
        Http.Request request = new TestableRequestBuilder().setCookies(cookies).build();
        Configuration config = configuration(Collections.singletonMap("sphere.countries", Arrays.asList("AT")));
        assertThat(controller.country(request, config)).isEqualTo(CountryCode.ES);
    }

    @Test
    public void userCountryIsDefaultWhenNoCookieFound() {
        Http.Request request = new TestableRequestBuilder().build();
        Configuration config = configuration(Collections.singletonMap("sphere.countries", Arrays.asList("AT")));
        assertThat(controller.country(request, config)).isEqualTo(CountryCode.AT);
    }

    @Test
    public void availableCountriesSkipsInvalidCountryFromConfiguration() {
        Configuration config = configuration(Collections.singletonMap("sphere.countries", Arrays.asList("INVALID","UK")));
        assertThat(controller.availableCountries(config)).containsExactly(CountryCode.UK);
    }

    @Test
    public void availableCountriesGetsAllCountriesFromConfiguration() {
        Configuration config = configuration(Collections.singletonMap("sphere.countries", Arrays.asList("AT","UK")));
        assertThat(controller.availableCountries(config)).containsExactly(CountryCode.AT, CountryCode.UK);
    }

    @Test
    public void defaultCountryIsFirstCountryFromConfiguration() {
        Configuration config = configuration(Collections.singletonMap("sphere.countries", Arrays.asList("AT","UK")));
        assertThat(controller.defaultCountry(config)).isEqualTo(CountryCode.AT);
    }

    @Test(expected=RuntimeException.class)
    public void defaultCountryThrowsExceptionWhenNoneConfigured() {
        Configuration config = configuration(Collections.emptyMap());
        controller.defaultCountry(config);
    }

    @Test
    public void countryCookieNameIsNameFromConfiguration() {
        Configuration config = configuration(Collections.singletonMap("shop.country.cookie", "ANOTHER_COOKIE_NAME"));
        assertThat(controller.countryCookieName(config)).isEqualTo("ANOTHER_COOKIE_NAME");
    }

    @Test
    public void countryCookieNameIsDefaultWhenNoneConfigured() {
        Configuration config = configuration(Collections.emptyMap());
        assertThat(controller.countryCookieName(config)).isEqualTo("SHOP_COUNTRY");
    }

    private Http.Cookie countryCookie(String name, CountryCode country) {
        return new Http.Cookie(name, country.getAlpha2(), null, null, null, false, false);
    }

    private Configuration configuration(Map<String, Object> configMap) {
        return new Configuration(ConfigFactory.parseMap(configMap));
    }
}
