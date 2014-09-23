package controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.neovisionaries.i18n.CountryCode;
import comparators.ByNameCategoryComparator;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.play.controllers.ShopController;
import models.CommonDataBuilder;

import io.sphere.sdk.categories.Category;
import models.UserContext;
import play.Configuration;
import play.Logger;
import play.Play;
import play.mvc.Http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
public class SunriseController extends ShopController {
    private final CategoryTree categoryTree;
    private CountryCode country;

    protected SunriseController(final PlayJavaClient client, final CategoryTree categoryTree) {
        super(client);
        this.categoryTree = categoryTree;
    }

    protected final CategoryTree categories() {
        return categoryTree;
    }

    protected final CommonDataBuilder data() {
        final ByNameCategoryComparator comparator = new ByNameCategoryComparator(lang().toLocale());
        final ImmutableList<Category> categories = Ordering.from(comparator).immutableSortedCopy(categories().getRoots());
        return CommonDataBuilder.of(userContext(), categories);
    }

    protected final UserContext userContext() {
        return UserContext.of(lang(), country());
    }

    protected final CountryCode country() {
        return country(request(), Play.application().configuration());
    }

    protected final void changeCountry(String country) {
        Optional<CountryCode> countryCode = parseCountryCode(country);
        if (countryCode.isPresent()) {
            changeCountry(countryCode.get(), response(), Play.application().configuration());
        }
    }

    /**
     * Sets the country associated with the user.
     * @param country the desired country for the user.
     * @param response the HTTP response to the user.
     * @param config the configuration of this shop.
     */
    protected void changeCountry(CountryCode country, Http.Response response, Configuration config) {
        if (availableCountries(config).contains(country)) {
            this.country = country;
            response.setCookie(countryCookieName(config), country.getAlpha2());
        }
    }

    /**
     * Gets the country associated with the user.
     * @param request the incoming HTTP request.
     * @param config the configuration of this shop.
     * @return the current country if any, or the country stored in the user's cookie, or the default country of the shop if none stored.
     */
    protected CountryCode country(Http.Request request, Configuration config) {
        if (this.country == null) {
            this.country = countryInCookie(request, config).orElse(defaultCountry(config));
        }
        return this.country;
    }

    /**
     * Gets the list of available countries for this shop, as defined in the configuration file.
     * @param config the configuration of this shop.
     * @return the list of available countries.
     */
    protected List<CountryCode> availableCountries(Configuration config) {
        List<String> configCountries = config.getStringList("sphere.countries", Collections.emptyList());
        List<CountryCode> countries = new ArrayList<>();
        for (String configCountry : configCountries) {
            Optional<CountryCode> country = parseCountryCode(configCountry);
            if (country.isPresent()) {
                countries.add(country.get());
            }
        }
        return countries;
    }

    /**
     * Gets the country stored in the user's cookie.
     * @param request the incoming HTTP request.
     * @param config the the configuration of this shop.
     * @return the country stored in the user's cookie, or absent if none stored.
     */
    protected Optional<CountryCode> countryInCookie(Http.Request request, Configuration config) {
        final Http.Cookie cookieCountry = request.cookie(countryCookieName(config));
        if (cookieCountry != null) {
            return parseCountryCode(cookieCountry.value());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Gets the default country for this shop, as defined in the configuration file.
     * @param config the configuration of this shop.
     * @return the first valid country defined in the configuration file.
     */
    protected CountryCode defaultCountry(Configuration config) {
        List<CountryCode> availableCountries = availableCountries(config);
        if (!availableCountries.isEmpty()) {
            return availableCountries.get(0);
        } else {
            throw new RuntimeException("No valid country defined in configuration file");
        }
    }

    /**
     * Gets the name of the cookie containing country information.
     * @param config the configuration of this shop.
     * @return the name of the cookie as defined in the configuration file, or the default name if none defined.
     */
    protected String countryCookieName(Configuration config) {
        return config.getString("shop.country.cookie", "SHOP_COUNTRY");
    }

    /**
     * Parses a country code as string.
     * @param countryCodeAsString the string representing a country code.
     * @return the country code represented in the string, or absent if it does not correspond to a valid country.
     */
    public static Optional<CountryCode> parseCountryCode(String countryCodeAsString) {
        try {
            return Optional.of(CountryCode.valueOf(countryCodeAsString));
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid country " + countryCodeAsString);
            return Optional.empty();
        }
    }
}
