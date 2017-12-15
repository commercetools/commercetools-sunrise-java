package com.commercetools.sunrise.models.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.core.viewmodels.forms.AbstractFormOption;

final class BucketRangeFacetedSearchFormOptionImpl extends AbstractFormOption<String> implements BucketRangeFacetedSearchFormOption {

    BucketRangeFacetedSearchFormOptionImpl(final String fieldLabel, final String fieldValue, final String range) {
        super(fieldLabel, fieldValue, range, false);
    }
}
