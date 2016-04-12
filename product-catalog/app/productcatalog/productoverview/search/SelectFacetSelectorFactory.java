package productcatalog.productoverview.search;

import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.CategoryTreeFacetOptionMapper;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetOptionMapper;
import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class SelectFacetSelectorFactory extends FacetSelectorFactory<SelectFacetBuilder<ProductProjection>> {

    private final SelectFacetConfig config;
    private final List<Category> selectedCategories;
    private final CategoryTree categoryTree;

    public SelectFacetSelectorFactory(final Map<String, List<String>> queryString, final SelectFacetConfig config,
                                      final List<Category> selectedCategories, final UserContext userContext,
                                      final CategoryTree categoryTree) {
        super(queryString, config, userContext);
        this.config = config;
        this.selectedCategories = selectedCategories;
        this.categoryTree = categoryTree;
    }

    public static SelectFacetSelectorFactory of(final SelectFacetConfig facetConfig, final Map<String, List<String>> queryString,
                                                final List<Category> selectedCategories, final UserContext userContext, final CategoryTree categoryTree) {
        return new SelectFacetSelectorFactory(queryString, facetConfig, selectedCategories, userContext, categoryTree);
    }

    @Override
    protected List<String> selectedValues() {
        final FacetOptionMapper mapper = config.getFacetBuilder().getMapper();
        if (mapper != null && mapper instanceof CategoryTreeFacetOptionMapper) {
            return selectedCategories.stream()
                    .flatMap(category -> categoryTree.getSubtree(singletonList(category)).getAllAsFlatList().stream())
                    .map(Category::getId)
                    .distinct()
                    .collect(toList());
        } else {
            return super.selectedValues();
        }
    }

    @Override
    protected Facet<ProductProjection> initializeFacet() {
        return config.getFacetBuilder()
                .mapper(configuredMapper().orElse(null))
                .facetedSearchSearchModel(localizedSearchModel())
                .build();
    }

    private FacetedSearchSearchModel<ProductProjection> localizedSearchModel() {
//        final SearchModel<ProductProjection> searchModel = config.getFacetBuilder().getFacetedSearchSearchModel().getSearchModel();
//        final String localizedAttrPath = localizeExpression(searchModel.getAttrPath(), userContext.locale());
//        return TermFacetedSearchSearchModel.of(localizedAttrPath);
        return config.getFacetBuilder().getFacetedSearchSearchModel();
    }

    private Optional<FacetOptionMapper> configuredMapper() {
        return Optional.ofNullable(config.getFacetBuilder().getMapper()).map(mapper -> {
            if (mapper instanceof CategoryTreeFacetOptionMapper) {
                final CategoryTree categoriesInFacet = getCategoriesInFacet(selectedCategories);
                return ((CategoryTreeFacetOptionMapper) mapper).withCategories(selectedCategories, categoriesInFacet, userContext.locales());
            } else {
                return mapper;
            }
        });
    }

    /**
     * Forms the category tree displayed in the category facet. That is, the tree containing:
     * - the category itself
     * - the ancestors tree
     * - the direct children categories
     * - the sibling categories
     * @param selectedCategories list of the selected categories
     * @return the new category tree containing only those categories displayed in the category facet
     */
    private CategoryTree getCategoriesInFacet(final List<Category> selectedCategories) {
        final List<Category> categoriesInFacet = selectedCategories.stream()
                .flatMap(category -> {
                    final List<Category> categories = new ArrayList<>();
                    categories.add(category);
                    categories.addAll(category.getAncestors().stream()
                            .map(c -> categoryTree.findById(c.getId()).orElse(null))
                            .filter(c -> c != null)
                            .collect(toList()));
                    categories.addAll(categoryTree.findChildren(category));
                    categories.addAll(categoryTree.findSiblings(singletonList(category)));
                    return categories.stream();
                })
                .distinct()
                .collect(toList());
        return CategoryTree.of(categoriesInFacet);
    }
}
