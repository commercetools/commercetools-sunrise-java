package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.viewmodels;

import com.commercetools.sunrise.ctp.categories.NavigationCategoryTree;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.CategoryTreeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.AbstractTermFacetViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.search.TermFacetResult;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Mapper that transforms facet options with Category IDs into a hierarchical list of facet options.
 * The IDs are then replaced by the Category name, in a language according to the provided locales.
 */
@RequestScoped
public final class CategoryTreeFacetViewModelFactory extends AbstractTermFacetViewModelFactory<CategoryTreeFacetedSearchFormSettings> {

    private final CategoryTree categoryTree;
    private final CategoryTreeFacetOptionViewModelFactory categoryTreeFacetOptionViewModelFactory;

    @Inject
    public CategoryTreeFacetViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver,
                                             @NavigationCategoryTree final CategoryTree categoryTree,
                                             final CategoryTreeFacetOptionViewModelFactory categoryTreeFacetOptionViewModelFactory) {
        super(i18nIdentifierResolver);
        this.categoryTree = categoryTree;
        this.categoryTreeFacetOptionViewModelFactory = categoryTreeFacetOptionViewModelFactory;
    }

    @Override
    protected List<FacetOptionViewModel> createOptions(final CategoryTreeFacetedSearchFormSettings settings, final TermFacetResult facetResult) {
        final Category selectedValue = settings.getSelectedValue(Http.Context.current()).orElse(null);
        return categoryTree.getSubtreeRoots().stream()
                .map(root -> categoryTreeFacetOptionViewModelFactory.create(facetResult, root, selectedValue))
                .filter(root -> root.getCount() > 0)
                .collect(toList());
    }
}
