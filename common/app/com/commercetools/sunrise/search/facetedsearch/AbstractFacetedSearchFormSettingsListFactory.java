package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.ConfiguredBucketRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.ConfiguredSliderRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.SliderRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.ConfiguredTermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettingsFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFacetedSearchFormSettingsListFactory<T> {

    private final TermFacetedSearchFormSettingsFactory termFacetedSearchFormSettingsFactory;
    private final SliderRangeFacetedSearchFormSettingsFactory sliderRangeFacetedSearchFormSettingsFactory;
    private final BucketRangeFacetedSearchFormSettingsFactory bucketRangeFacetedSearchFormSettingsFactory;

    protected AbstractFacetedSearchFormSettingsListFactory(final TermFacetedSearchFormSettingsFactory termFacetedSearchFormSettingsFactory,
                                                           final SliderRangeFacetedSearchFormSettingsFactory sliderRangeFacetedSearchFormSettingsFactory,
                                                           final BucketRangeFacetedSearchFormSettingsFactory bucketRangeFacetedSearchFormSettingsFactory) {
        this.termFacetedSearchFormSettingsFactory = termFacetedSearchFormSettingsFactory;
        this.sliderRangeFacetedSearchFormSettingsFactory = sliderRangeFacetedSearchFormSettingsFactory;
        this.bucketRangeFacetedSearchFormSettingsFactory = bucketRangeFacetedSearchFormSettingsFactory;
    }

    public FacetedSearchFormSettingsList<T> create(final ConfiguredFacetedSearchFormSettingsList configuration) {
        final List<FacetedSearchFormSettings<T>> list = new ArrayList<>();
        configuration.getConfiguredSettings().forEach(settings -> addSettingsToList(list, settings));
        return new FacetedSearchFormSettingsListImpl<>(list);
    }

    protected void addSettingsToList(final List<FacetedSearchFormSettings<T>> list, final ConfiguredFacetedSearchFormSettings settings) {
        if (settings instanceof ConfiguredTermFacetedSearchFormSettings) {
            list.add(termFacetedSearchFormSettingsFactory.create((ConfiguredTermFacetedSearchFormSettings) settings));
        } else if (settings instanceof ConfiguredSliderRangeFacetedSearchFormSettings) {
            list.add(sliderRangeFacetedSearchFormSettingsFactory.create((ConfiguredSliderRangeFacetedSearchFormSettings) settings));
        } else if (settings instanceof ConfiguredBucketRangeFacetedSearchFormSettings) {
            list.add(bucketRangeFacetedSearchFormSettingsFactory.create((ConfiguredBucketRangeFacetedSearchFormSettings) settings));
        }
    }
}
