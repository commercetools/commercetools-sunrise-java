package controllers;

import com.neovisionaries.i18n.CountryCode;
import exceptions.DefaultCountryNotFound;
import exceptions.InvalidCountryCode;
import play.Configuration;
import play.Logger;
import play.mvc.Http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CountryOperations {
    static final String COUNTRY_SESSION_KEY = "SHOP_COUNTRY";
    static final String COUNTRY_CONFIG_LIST = "sphere.countries";
    public static final Logger.ALogger LOGGER = Logger.of(CountryOperations.class);
    private final Configuration configuration;

    private CountryOperations(Configuration configuration) {
        this.configuration = configuration;
    }

    public static CountryOperations of(Configuration configuration) {
        return new CountryOperations(configuration);
    }

    /**
     * Gets the country associated with the user.
     * @return the country stored in session, or the default country of the shop if none stored.
     */
    public CountryCode country() {
        return countryInSession().orElse(defaultCountry());
    }

    /**
     * Gets the list of available countries for this shop, as defined in the configuration file.
     * @return the list of available countries.
     */
    public List<CountryCode> availableCountries() {
        List<CountryCode> countries = new ArrayList<>();
        for (String configCountry : configCountryList()) {
            parseCode(configCountry).ifPresent(countries::add);
        }
        return countries;
    }

    /**
     * Gets the default country for this shop, as defined in the configuration file.
     * @return the first valid country defined in the configuration file.
     * @throws exceptions.DefaultCountryNotFound when a default valid country could not be found.
     */
    public CountryCode defaultCountry() {
        List<CountryCode> availableCountries = availableCountries();
        if (!availableCountries.isEmpty()) {
            return availableCountries.get(0);
        } else {
            throw new DefaultCountryNotFound();
        }
    }

    /**
     * Sets the country associated with the user, if a valid country code is provided.
     * @param countryCodeAsString the string representing a country code.
     * @throws exceptions.InvalidCountryCode when the country code does not correspond to a valid country.
     */
    public void changeCountry(String countryCodeAsString) {
        CountryCode countryCode = parseCode(countryCodeAsString)
                .orElseThrow(() -> new InvalidCountryCode(countryCodeAsString));
        changeCountry(countryCode);
    }

    /**
     * Parses a country code as string.
     * @param countryCodeAsString the string representing a country code.
     * @return the country code represented in the string, or absent if it does not correspond to a valid country.
     */
    public static Optional<CountryCode> parseCode(String countryCodeAsString) {
        try {
            return Optional.of(CountryCode.valueOf(countryCodeAsString));
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid country " + countryCodeAsString);
            return Optional.empty();
        }
    }

    /**
     * Sets the country associated with the user.
     * @param countryCode the desired country for the user.
     */
    private void changeCountry(CountryCode countryCode) {
        if (availableCountries().contains(countryCode)) {
            Http.Context.current().session().put(COUNTRY_SESSION_KEY, countryCode.getAlpha2());
        }
    }

    /**
     * Gets the country stored in session.
     * @return the country stored in the session, or absent if none stored.
     */
    private Optional<CountryCode> countryInSession() {
        Http.Session session = Http.Context.current().session();
        Optional<String> code = Optional.ofNullable(session.get(COUNTRY_SESSION_KEY));
        return code.flatMap(CountryOperations::parseCode);
    }

    /**
     * Gets the list of countries for this project.
     * @return the list of countries as defined in the configuration file, or empty list if none defined.
     */
    private List<String> configCountryList() {
        return configuration.getStringList(COUNTRY_CONFIG_LIST, Collections.emptyList());
    }
}
