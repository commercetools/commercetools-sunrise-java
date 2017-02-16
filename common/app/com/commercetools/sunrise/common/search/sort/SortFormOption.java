package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.common.forms.FormOption;
import com.commercetools.sunrise.common.search.SearchUtils;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public final class SortFormOption extends FormOption<List<SortExpression<ProductProjection>>> {

    private SortFormOption(final String fieldLabel, final String fieldValue, final List<SortExpression<ProductProjection>> expressions, final boolean isDefault) {
        super(fieldLabel, fieldValue, expressions, isDefault);
    }

    @Override
    public String getFieldLabel() {
        return super.getFieldLabel();
    }

    @Override
    public String getFieldValue() {
        return super.getFieldValue();
    }

    /**
     * Gets the sort model associated with this option, representing the attribute path and sorting direction.
     * The expressions might contain {@code {{locale}}}, which should be replaced with the current locale before using them.
     * @return the sort model for this option
     */
    @Override
    public List<SortExpression<ProductProjection>> getValue() {
        return super.getValue();
    }

    public List<SortExpression<ProductProjection>> getLocalizedValue(final Locale locale) {
        return getValue().stream()
                .map(expr -> SortExpression.<ProductProjection>of(SearchUtils.localizeExpression(expr.expression(), locale)))
                .collect(toList());
    }

    @Override
    public boolean isDefault() {
        return super.isDefault();
    }

    public static SortFormOption of(final String fieldLabel, final String fieldValue, final List<SortExpression<ProductProjection>> expressions, final boolean isDefault) {
        return new SortFormOption(fieldLabel, fieldValue, expressions, isDefault);
    }
}
