package com.commercetools.sunrise.models.search.facetedsearch.bucketranges.viewmodels;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.forms.FormOption;
import com.commercetools.sunrise.models.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.AbstractFacetWithOptionsViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.RangeFacetResult;
import io.sphere.sdk.search.model.RangeStats;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.models.search.facetedsearch.RangeUtils.mapRangeToStats;
import static com.commercetools.sunrise.models.search.facetedsearch.RangeUtils.parseFacetRange;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class BucketRangeFacetViewModelFactory extends AbstractFacetWithOptionsViewModelFactory<BucketRangeFacetViewModel, BucketRangeFacetedSearchFormSettings<?>, RangeFacetResult> {

    private final BucketRangeFacetOptionViewModelFactory bucketRangeFacetOptionViewModelFactory;

    @Inject
    public BucketRangeFacetViewModelFactory(final I18nResolver i18nResolver,
                                            final BucketRangeFacetOptionViewModelFactory bucketRangeFacetOptionViewModelFactory) {
        super(i18nResolver);
        this.bucketRangeFacetOptionViewModelFactory = bucketRangeFacetOptionViewModelFactory;
    }

    protected final BucketRangeFacetOptionViewModelFactory getBucketRangeFacetOptionViewModelFactory() {
        return bucketRangeFacetOptionViewModelFactory;
    }

    @Override
    protected BucketRangeFacetViewModel newViewModelInstance(final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        return new BucketRangeFacetViewModel();
    }

    @Override
    public final BucketRangeFacetViewModel create(final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        return super.create(settings, facetResult);
    }

    @Override
    protected final void initialize(final BucketRangeFacetViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        super.initialize(viewModel, settings, facetResult);
        fillKey(viewModel, settings, facetResult);
    }

    protected void fillKey(final BucketRangeFacetViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        viewModel.setKey(settings.getFieldName());
    }

    @Override
    protected void fillAvailable(final BucketRangeFacetViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        viewModel.setAvailable(facetResult.getRanges().stream().mapToLong(RangeStats::getCount).sum() > 0);
    }

    @Override
    protected void fillLimitedOptions(final BucketRangeFacetViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        final List<String> selectedValues = settings.getAllSelectedOptions(Http.Context.current()).stream()
                .map(FormOption::getFieldValue)
                .collect(toList());
        final Map<String, RangeStats> rangeToStatsMap = mapRangeToStats(facetResult);
        final List<FacetOptionViewModel> options = new ArrayList<>();
        settings.getOptions()
                .forEach(option -> parseFacetRange(option.getValue())
                        .map(range -> range.toString())
                        .filter(rangeToStatsMap::containsKey)
                        .map(rangeToStatsMap::get)
                        .ifPresent(rangeStats -> options.add(bucketRangeFacetOptionViewModelFactory.create(rangeStats, option, selectedValues))));
        viewModel.setLimitedOptions(options);
    }
}
