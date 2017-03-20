package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;

public interface BucketRangeFacetedSearchFormOption extends FormOption<String> {

    static BucketRangeFacetedSearchFormOption of(final String fieldLabel, final String fieldValue, final String range) {
        return new BucketRangeFacetedSearchFormOptionImpl(fieldLabel, fieldValue, range);
    }
}
