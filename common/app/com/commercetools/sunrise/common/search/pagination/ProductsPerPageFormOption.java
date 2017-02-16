package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.forms.FormOption;

public final class ProductsPerPageFormOption extends FormOption<Integer> {

    private ProductsPerPageFormOption(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
        super(fieldLabel, fieldValue, value, isDefault);
    }

    @Override
    public String getFieldValue() {
        return super.getFieldValue();
    }

    @Override
    public String getFieldLabel() {
        return super.getFieldLabel();
    }

    @Override
    public Integer getValue() {
        return super.getValue();
    }

    @Override
    public boolean isDefault() {
        return super.isDefault();
    }

    public static ProductsPerPageFormOption of(final String fieldLabel, final String fieldValue, final Integer value, final boolean isDefault) {
        return new ProductsPerPageFormOption(fieldLabel, fieldValue, value, isDefault);
    }
}
