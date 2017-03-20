package com.commercetools.sunrise.search.facetedsearch.bucketranges.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.framework.viewmodels.forms.FormOption;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetWithOptionsViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.RangeFacetResult;
import io.sphere.sdk.search.model.FacetRange;
import io.sphere.sdk.search.model.RangeStats;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.commercetools.sunrise.search.facetedsearch.RangeUtils.mapRangeToStats;
import static com.commercetools.sunrise.search.facetedsearch.bucketranges.BucketRangeUtils.optionToFacetRange;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class BucketRangeFacetViewModelFactory extends AbstractFacetWithOptionsViewModelFactory<BucketRangeFacetViewModel, BucketRangeFacetedSearchFormSettings<?>, RangeFacetResult> {

    private final Http.Context httpContext;
    private final BucketRangeFacetOptionViewModelFactory bucketRangeFacetOptionViewModelFactory;

    @Inject
    public BucketRangeFacetViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver, final Http.Context httpContext,
                                            final BucketRangeFacetOptionViewModelFactory bucketRangeFacetOptionViewModelFactory) {
        super(i18nIdentifierResolver);
        this.httpContext = httpContext;
        this.bucketRangeFacetOptionViewModelFactory = bucketRangeFacetOptionViewModelFactory;
    }

    protected final Http.Context getHttpContext() {
        return httpContext;
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
        final List<String> selectedValues = settings.getAllSelectedOptions(httpContext).stream()
                .map(FormOption::getFieldValue)
                .collect(toList());
        final Map<FacetRange<String>, RangeStats> rangeToStatsMap = mapRangeToStats(facetResult);
        final List<FacetOptionViewModel> options = new ArrayList<>();
        settings.getOptions()
                .forEach(option -> optionToFacetRange(option)
                        .map(rangeToStatsMap::get)
                        .filter(Objects::nonNull)
                        .ifPresent(rangeStats -> options.add(bucketRangeFacetOptionViewModelFactory.create(rangeStats, option, selectedValues))));
        viewModel.setLimitedOptions(options);
    }
}
