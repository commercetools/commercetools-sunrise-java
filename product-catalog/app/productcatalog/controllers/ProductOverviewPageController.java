package productcatalog.controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import io.sphere.sdk.models.LocalizedStringEntry;
import productcatalog.models.*;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.*;
import play.Logger;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends ProductCatalogController {
    private final int paginationDisplayedPages;

    @Inject
    public ProductOverviewPageController(final ControllerDependency controllerDependency, final ProductProjectionService productService,
                                         final CategoryService categoryService, final ProductDataConfig productDataConfig) {
        super(controllerDependency, categoryService, productService, productDataConfig);
        this.paginationDisplayedPages = configuration().getInt("pop.pagination.displayedPages", 6);
    }

    public F.Promise<Result> show(final String languageTag, final int page, final String categorySlug) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final Optional<Category> category = categories().findBySlug(userContext.locale(), categorySlug);
        if (category.isPresent()) {
            final SearchOperations searchOps = new SearchOperations(configuration(), request(), messages, userContext.locale(), getCategoriesInFacet(category));
            return searchProductsByCategory(page, searchOps, category.get()).map(searchResult -> {
                final ProductOverviewPageContent content = getPopPageData(userContext, searchResult, page, searchOps, category.get());
                return ok(templateService().renderToHtml("pop", pageData(userContext, content, ctx()), userContext.locales()));
            });
        }
        return F.Promise.pure(notFound("Category not found: " + categorySlug));
    }

    public F.Promise<Result> search(final String languageTag, final int page) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final SearchOperations searchOps = new SearchOperations(configuration(), request(), messages, userContext.locale(), categories());
        if (searchOps.searchTerm().isPresent()) {
            final LocalizedStringEntry searchTerm = searchOps.searchTerm().get();
            final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = searchProductsWithFullText(page, searchOps, searchTerm);
            return searchResultPromise.map(searchResult -> {
                final ProductOverviewPageContent content = getPopPageData(userContext, searchResult, page, searchOps, searchTerm.getValue());
                return ok(templateService().renderToHtml("pop", pageData(userContext, content, ctx()), userContext.locales()));
            });
        } else {
            return F.Promise.pure(badRequest("Search term missing"));
        }
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext, final PagedSearchResult<ProductProjection> searchResult,
                                                      final int page, final SearchOperations searchOps, final Category category) {
        final String title = category.getName().find(userContext.locales()).orElse("");
        final ProductOverviewPageContent content = createPageContent(userContext, title, searchResult, page, searchOps);
        content.setBreadcrumb(new BreadcrumbData(category, categories(), userContext, reverseRouter()));
        content.setJumbotron(new JumbotronData(category, userContext, categories()));
        content.setBanner(createBanner(userContext, category));
        content.setSeo(new SeoData(userContext, category));
        return content;
    }

    private ProductOverviewPageContent getPopPageData(final UserContext userContext, final PagedSearchResult<ProductProjection> searchResult,
                                                      final int page, final SearchOperations searchOps, final String searchTerm) {
        final String title = searchTerm;
        final ProductOverviewPageContent content = createPageContent(userContext, title, searchResult, page, searchOps);
        content.setBreadcrumb(new BreadcrumbData(searchTerm));
        content.setSearchTerm(searchTerm);
        return content;
    }

    private ProductOverviewPageContent createPageContent(final UserContext userContext, final String title,
                                                         final PagedSearchResult<ProductProjection> searchResult,
                                                         final int page, final SearchOperations searchOps) {
        final ProductOverviewPageContent content = new ProductOverviewPageContent(title);
        content.setFilterProductsUrl(request().path());
        content.setProducts(new ProductListData(searchResult.getResults(), productDataConfig(), userContext, reverseRouter(), categoryTreeInNew()));
        content.setPagination(new PaginationData(requestContext(), searchResult, page, searchOps.selectedDisplay(), paginationDisplayedPages));
        content.setSortSelector(searchOps.boundSortSelector());
        content.setDisplaySelector(searchOps.boundDisplaySelector());
        content.setFacets(new FacetListData(searchResult, searchOps.boundFacets()));
        return content;
    }

    /* Move to product service */

    private F.Promise<PagedSearchResult<ProductProjection>> searchProductsByCategory(final int page, final SearchOperations searchOps,
                                                                                     final Category category) {
        final List<String> categoriesId = categoryService().getSubtree(singletonList(category)).getAllAsFlatList().stream()
                .map(Category::getId)
                .collect(toList());
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withQueryFilters(filter -> filter.categories().id().byAny(categoriesId));
        return searchProducts(request, page, searchOps);
    }

    private F.Promise<PagedSearchResult<ProductProjection>> searchProductsWithFullText(final int page, final SearchOperations searchOps,
                                                                                       final LocalizedStringEntry searchTerm) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent()
                .withText(searchTerm);
        return searchProducts(request, page, searchOps);
    }

    private F.Promise<PagedSearchResult<ProductProjection>> searchProducts(final ProductProjectionSearch baseSearchRequest,
                                                                           final int page, final SearchOperations searchOps) {
        final int pageSize = searchOps.selectedDisplay();
        final int offset = (page - 1) * pageSize;
        final ProductProjectionSearch searchRequest = baseSearchRequest
                .withFacetedSearch(searchOps.selectedFacets())
                .withSort(searchOps.selectedSort())
                .withOffset(offset)
                .withLimit(pageSize);
        final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = sphere().execute(searchRequest);
        searchResultPromise.onRedeem(result -> Logger.debug("Fetched {} out of {} products with request {}",
                result.size(),
                result.getTotal(),
                searchRequest.httpRequestIntent().getPath()));
        return searchResultPromise;
    }

    private static BannerData createBanner(final UserContext userContext, final Category category) {
        final BannerData bannerData = new BannerData(userContext, category);
        bannerData.setImageMobile("/assets/img/banner_mobile.jpg"); // TODO obtain from category?
        bannerData.setImageDesktop("/assets/img/banner_desktop.jpg"); // TODO obtain from category?
        return bannerData;
    }

    private CategoryTree getCategoriesInFacet(final Optional<Category> category) {
        final Category rootAncestor = categoryService().getRootAncestor(category.get());
        return categoryService().getSubtree(singletonList(rootAncestor));
    }
}