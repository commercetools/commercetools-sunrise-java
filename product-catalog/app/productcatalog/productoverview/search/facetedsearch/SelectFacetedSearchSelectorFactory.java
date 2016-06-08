package productcatalog.productoverview.search.facetedsearch;

import common.contexts.RequestContext;
import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.CategoryTreeFacetOptionMapper;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.FacetOptionMapper;
import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;
import io.sphere.sdk.search.model.SearchModel;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import productcatalog.productoverview.search.SearchUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static productcatalog.productoverview.search.SearchUtils.localizeExpression;

public class SelectFacetedSearchSelectorFactory extends FacetedSearchSelectorFactory<SelectFacetBuilder<ProductProjection>> {

    private final SelectFacetedSearchConfig config;
    private final List<Category> selectedCategories;
    private final UserContext userContext;
    private final CategoryTree categoryTree;
    private final RequestContext requestContext;

    protected SelectFacetedSearchSelectorFactory(final SelectFacetedSearchConfig facetConfig, final UserContext userContext,
                                                 final RequestContext requestContext, final CategoryTree categoryTree, final List<Category> selectedCategories) {
        super(facetConfig);
        this.config = facetConfig;
        this.categoryTree = categoryTree;
        this.userContext = userContext;
        this.requestContext = requestContext;
        this.selectedCategories = selectedCategories;
    }

    public static SelectFacetedSearchSelectorFactory of(final SelectFacetedSearchConfig facetConfig, final UserContext userContext,
                                                        final RequestContext requestContext, final CategoryTree categoryTree,
                                                        final List<Category> selectedCategories) {
        return new SelectFacetedSearchSelectorFactory(facetConfig, userContext, requestContext, categoryTree, selectedCategories);
    }

    protected List<String> selectedValues() {
        final FacetOptionMapper mapper = config.getFacetBuilder().getMapper();
        if (mapper != null && mapper instanceof CategoryTreeFacetOptionMapper) {
            return selectedCategories.stream()
                    .flatMap(category -> categoryTree.getSubtree(singletonList(category)).getAllAsFlatList().stream())
                    .map(Category::getId)
                    .distinct()
                    .collect(toList());
        } else {
            return SearchUtils.selectedValues(key(), requestContext);
        }
    }

    @Override
    protected Facet<ProductProjection> initializeFacet() {
        return config.getFacetBuilder()
                .mapper(configuredMapper().orElse(null))
                .facetedSearchSearchModel(localizedSearchModel())
                .selectedValues(selectedValues())
                .build();
    }

    protected FacetedSearchSearchModel<ProductProjection> localizedSearchModel() {
        final SearchModel<ProductProjection> searchModel = config.getFacetBuilder().getFacetedSearchSearchModel().getSearchModel();
        final String localizedAttrPath = localizeExpression(searchModel.attributePath(), userContext.locale());
        return TermFacetedSearchSearchModel.of(localizedAttrPath);
    }

    protected Optional<FacetOptionMapper> configuredMapper() {
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
    protected CategoryTree getCategoriesInFacet(final List<Category> selectedCategories) {
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
