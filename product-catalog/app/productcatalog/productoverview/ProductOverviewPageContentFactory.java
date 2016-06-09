package productcatalog.productoverview;

import common.contexts.UserContext;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import productcatalog.common.BreadcrumbBeanFactory;
import productcatalog.common.ProductListBeanFactory;
import productcatalog.productoverview.search.SearchCriteriaImpl;
import productcatalog.productoverview.search.facetedsearch.FacetSelectorBean;
import productcatalog.productoverview.search.facetedsearch.FacetSelectorListBean;
import productcatalog.productoverview.search.facetedsearch.FacetedSearchSelector;
import productcatalog.productoverview.search.facetedsearch.FacetedSearchSelectorListFactory;
import productcatalog.productoverview.search.productsperpage.ProductsPerPageSelectorFactory;
import productcatalog.productoverview.search.sort.SortSelectorFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductOverviewPageContentFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private BreadcrumbBeanFactory breadcrumbBeanFactory;
    @Inject
    private ProductListBeanFactory productListBeanFactory;

    // TODO Remove to avoid duplicated work
    @Inject
    private SortSelectorFactory sortSelectorFactory;
    @Inject
    private ProductsPerPageSelectorFactory productsPerPageSelectorFactory;
    @Inject
    private FacetedSearchSelectorListFactory facetedSearchSelectorListFactory;

    public ProductOverviewPageContent create(final Category category, final PagedSearchResult<ProductProjection> searchResult,
                                             final SearchCriteriaImpl searchCriteria) {
        final ProductOverviewPageContent content = new ProductOverviewPageContent();
        fill(Collections.singletonList(category), content, searchResult, searchCriteria);
        content.setAdditionalTitle(category.getName().find(userContext.locales()).orElse(""));
        content.setBreadcrumb(breadcrumbBeanFactory.create(category));
        content.setJumbotron(new JumbotronBean(category, userContext, categoryTree));
        content.setBanner(createBanner(category));
        content.setSeo(new SeoBean(userContext, category));
        return content;
    }

    public ProductOverviewPageContent create(final PagedSearchResult<ProductProjection> searchResult,
                                             final SearchCriteriaImpl searchCriteria) {
        final ProductOverviewPageContent content = new ProductOverviewPageContent();
        fill(Collections.emptyList(), content, searchResult, searchCriteria);
        final String searchTerm = searchCriteria.getSearchTerm().get().getValue();
        content.setAdditionalTitle(searchTerm);
        content.setBreadcrumb(breadcrumbBeanFactory.create(searchTerm));
        content.setSearchTerm(searchTerm);
        return content;
    }

    private void fill(final List<Category> selectedCategories, final ProductOverviewPageContent content, final PagedSearchResult<ProductProjection> searchResult,
                      final SearchCriteriaImpl searchCriteria) {
        content.setProducts(productListBeanFactory.create(searchResult.getResults()));
        content.setFacets(createFacetSelectorList(facetedSearchSelectorListFactory.create(selectedCategories), searchResult));
    }

    private FacetSelectorListBean createFacetSelectorList(final List<FacetedSearchSelector> facetedSearchSelectors, final PagedSearchResult<ProductProjection> searchResult) {
         final FacetSelectorListBean bean = new FacetSelectorListBean();
         bean.setList(facetedSearchSelectors.stream()
                 .sorted((f1, f2) -> Double.compare(f1.getPosition(), f2.getPosition()))
                 .map(facetedSearchSelector -> createFacetSelector(facetedSearchSelector, searchResult))
                 .collect(toList()));
         return bean;
    }

    private FacetSelectorBean createFacetSelector(final FacetedSearchSelector facetedSearchSelector, final PagedSearchResult<ProductProjection> searchResult) {
        final FacetSelectorBean bean = new FacetSelectorBean();
        final Facet<ProductProjection> facet = facetedSearchSelector.getFacet(searchResult);
        if (facet.getLabel() != null) {
            final String label = i18nResolver.getOrKey(userContext.locales(), I18nIdentifier.of(facet.getLabel()));
            bean.setFacet(facet.withLabel(label));
        } else {
            bean.setFacet(facet);
        }
        return bean;
    }

    private BannerBean createBanner(final Category category) {
        final BannerBean bannerBean = new BannerBean(userContext, category);
        bannerBean.setImageMobile("/assets/img/banner_mobile-0a9241da249091a023ecfadde951a53b.jpg"); // TODO obtain from category?
        bannerBean.setImageDesktop("/assets/img/banner_desktop-9ffd148c48068ce2666d6533b4a87d11.jpg"); // TODO obtain from category?
        return bannerBean;
    }
}
