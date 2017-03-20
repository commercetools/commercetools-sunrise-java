package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import com.commercetools.sunrise.framework.SunriseModel;
import play.Configuration;

import java.util.Optional;

public class EntriesPerPageFormOptionFactory extends SunriseModel {

    private static final String OPTION_FIELD_LABEL_ATTR = "fieldLabel";
    private static final String OPTION_FIELD_VALUE_ATTR = "fieldValue";
    private static final String OPTION_AMOUNT_ATTR = "amount";
    private static final String OPTION_DEFAULT_ATTR = "default";

    private static final int MIN_PAGE_SIZE = 0;
    private static final int MAX_PAGE_SIZE = 500;

    public EntriesPerPageFormOption create(final Configuration configuration) {
        final String fieldLabel = fieldLabel(configuration);
        final String fieldValue = fieldValue(configuration);
        final int amount = amount(configuration);
        final Boolean isDefault = isDefault(configuration);
        return new EntriesPerPageFormOptionImpl(fieldLabel, fieldValue, amount, isDefault);
    }

    protected final String fieldLabel(final Configuration configuration) {
        return configuration.getString(OPTION_FIELD_LABEL_ATTR, "");
    }

    protected final String fieldValue(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(OPTION_FIELD_VALUE_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing elements per page value", OPTION_FIELD_VALUE_ATTR, configuration));
    }

    protected final int amount(final Configuration configuration) {
        final int amount = Optional.ofNullable(configuration.getInt(OPTION_AMOUNT_ATTR))
                .orElseThrow(() -> new SunriseConfigurationException("Missing elements per page amount", OPTION_AMOUNT_ATTR, configuration));
        if (!isValidAmount(amount)) {
            throw new SunriseConfigurationException(String.format("Elements per page option is not within bounds [%d, %d]: %s",
                    MIN_PAGE_SIZE, MAX_PAGE_SIZE, amount), OPTION_AMOUNT_ATTR, configuration);
        }
        return amount;
    }

    protected final Boolean isDefault(final Configuration configuration) {
        return configuration.getBoolean(OPTION_DEFAULT_ATTR, false);
    }

    private static boolean isValidAmount(final int amount) {
        return amount >= MIN_PAGE_SIZE && amount <= MAX_PAGE_SIZE;
    }
}
