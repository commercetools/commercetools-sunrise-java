package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.SunriseModel;

import javax.annotation.Nullable;
import java.util.Locale;

import static com.commercetools.sunrise.search.SearchUtils.localizeExpression;

public abstract class AbstractFacetedSearchFormSettings<C extends ConfiguredFacetedSearchFormSettings> extends SunriseModel implements ConfiguredFacetedSearchFormSettings {

    private final C configuration;
    private final Locale locale;

    protected AbstractFacetedSearchFormSettings(final C configuration, final Locale locale) {
        this.configuration = configuration;
        this.locale = locale;
    }

    protected final Locale getLocale() {
        return locale;
    }

    public C configuration() {
        return configuration;
    }

    @Override
    public String getFieldLabel() {
        return configuration.getFieldLabel();
    }

    @Override
    public String getAttributePath() {
        return localizeExpression(configuration.getAttributePath(), locale);
    }

    @Override
    public boolean isCountDisplayed() {
        return configuration.isCountDisplayed();
    }

    @Override
    @Nullable
    public String getUIType() {
        return configuration.getUIType();
    }
}
