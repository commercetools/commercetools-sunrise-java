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

import java.util.List;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
public class SunriseController extends ShopController {
    private final CategoryTree categoryTree;

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
        return userCountry(request(), Play.application().configuration());
    }

    /**
     * Gets the country associated with the user.
     * @return the country stored in the user's cookie, or the default country of the shop if none stored.
     */
    protected CountryCode userCountry(Http.Request request, Configuration config) {
        Http.Cookie cookieCountry = request.cookie(countryCookieName(config));
        if (cookieCountry != null) {
            try {
                return CountryCode.valueOf(cookieCountry.value());
            } catch (IllegalArgumentException iae) {
                Logger.debug("Invalid country defined in cookie: " + cookieCountry);
            }
        }
        return defaultCountry(config);
    }

    /**
     * Gets the default country for this shop, as defined in the configuration file.
     * @return the first valid country defined in the configuration file.
     */
    protected CountryCode defaultCountry(Configuration config) {
        List<String> configCountries = config.getStringList("sphere.countries");
        for (String configCountry : configCountries) {
            try {
                return CountryCode.valueOf(configCountry);
            } catch (IllegalArgumentException iae) {
                Logger.error("Invalid country defined in configuration file: " + configCountry);
            }
        }
        throw new RuntimeException("No valid country defined in configuration file");
    }

    /**
     * Gets the name of the cookie containing country information.
     * @return the name of the cookie as defined in the configuration file, or the default name if none defined.
     */
    protected String countryCookieName(Configuration config) {
        return config.getString("shop.country.cookie", "SHOP_COUNTRY");
    }
}
