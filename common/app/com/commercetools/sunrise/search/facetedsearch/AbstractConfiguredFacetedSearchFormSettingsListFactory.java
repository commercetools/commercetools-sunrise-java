package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.commercetools.sunrise.framework.SunriseModel;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.ConfiguredBucketRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.ConfiguredSliderRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.ConfiguredTermFacetedSearchFormSettingsFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbstractConfiguredFacetedSearchFormSettingsListFactory extends SunriseModel {

    private final static String CONFIG_TYPE = "type";
    private final static String CONFIG_TYPE_TERM = "term";
    private final static String CONFIG_TYPE_SLIDER = "sliderRange";
    private final static String CONFIG_TYPE_BUCKET = "bucketRange";

    private final ConfiguredTermFacetedSearchFormSettingsFactory configuredTermFacetedSearchFormSettingsFactory;
    private final ConfiguredSliderRangeFacetedSearchFormSettingsFactory configuredSliderRangeFacetedSearchFormSettingsFactory;
    private final ConfiguredBucketRangeFacetedSearchFormSettingsFactory configuredBucketRangeFacetedSearchFormSettingsFactory;

    @Inject
    public AbstractConfiguredFacetedSearchFormSettingsListFactory(final ConfiguredTermFacetedSearchFormSettingsFactory configuredTermFacetedSearchFormSettingsFactory,
                                                                  final ConfiguredSliderRangeFacetedSearchFormSettingsFactory configuredSliderRangeFacetedSearchFormSettingsFactory,
                                                                  final ConfiguredBucketRangeFacetedSearchFormSettingsFactory configuredBucketRangeFacetedSearchFormSettingsFactory) {
        this.configuredTermFacetedSearchFormSettingsFactory = configuredTermFacetedSearchFormSettingsFactory;
        this.configuredSliderRangeFacetedSearchFormSettingsFactory = configuredSliderRangeFacetedSearchFormSettingsFactory;
        this.configuredBucketRangeFacetedSearchFormSettingsFactory = configuredBucketRangeFacetedSearchFormSettingsFactory;
    }

    public ConfiguredFacetedSearchFormSettingsList create(final List<Configuration> configurationList) {
        final List<ConfiguredFacetedSearchFormSettings> list = new ArrayList<>();
        configurationList.forEach(configuration -> addSettingsToList(list, configuration, type(configuration)));
        return new ConfiguredFacetedSearchFormSettingsListImpl(list);
    }

    protected void addSettingsToList(final List<ConfiguredFacetedSearchFormSettings> list, final Configuration configuration, final String type) {
        switch (type) {
            case CONFIG_TYPE_TERM:
                list.add(configuredTermFacetedSearchFormSettingsFactory.create(configuration));
                break;
            case CONFIG_TYPE_SLIDER:
                list.add(configuredSliderRangeFacetedSearchFormSettingsFactory.create(configuration));
                break;
            case CONFIG_TYPE_BUCKET:
                list.add(configuredBucketRangeFacetedSearchFormSettingsFactory.create(configuration));
                break;
            default:
                throw new SunriseConfigurationException("Unknown facet type \"" + type + "\"", CONFIG_TYPE, configuration);
        }
    }

    private static String type(final Configuration configuration) {
        return Optional.ofNullable(configuration.getString(CONFIG_TYPE))
                .orElseThrow(() -> new SunriseConfigurationException("Could not find required facet type", CONFIG_TYPE, configuration));
    }
}
