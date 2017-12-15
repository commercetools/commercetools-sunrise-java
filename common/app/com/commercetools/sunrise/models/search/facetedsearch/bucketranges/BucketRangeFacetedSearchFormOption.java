package com.commercetools.sunrise.models.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.core.viewmodels.forms.FormOption;

public interface BucketRangeFacetedSearchFormOption extends FormOption<String> {

    static BucketRangeFacetedSearchFormOption of(final String fieldLabel, final String fieldValue, final String range) {
        return new BucketRangeFacetedSearchFormOptionImpl(fieldLabel, fieldValue, range);
    }
}
