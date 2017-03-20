package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import com.commercetools.sunrise.framework.viewmodels.forms.AbstractFormOption;

final class BucketRangeFacetedSearchFormOptionImpl extends AbstractFormOption<String> implements BucketRangeFacetedSearchFormOption {

    BucketRangeFacetedSearchFormOptionImpl(final String fieldLabel, final String fieldValue, final String range) {
        super(fieldLabel, fieldValue, range, false);
    }
}
