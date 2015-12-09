package productcatalog.controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import play.twirl.api.Html;
import productcatalog.models.*;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.*;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;
import productcatalog.services.ProductService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductOverviewPageController extends ProductCatalogController {
    private final int paginationDisplayedPages;

    @Inject
    public ProductOverviewPageController(final ControllerDependency controllerDependency,
                                         final ProductService productService, final ProductDataConfig productDataConfig) {
        super(controllerDependency, productService, productDataConfig);
        this.paginationDisplayedPages = configuration().getInt("pop.pagination.displayedPages", 6);
    }

    /* Controller actions */

    public F.Promise<Result> show(final String languageTag, final int page, final String categorySlug) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final Optional<Category> category = categoryTree().findBySlug(userContext.locale(), categorySlug);
        if (category.isPresent()) {
            final CategoryTree categoriesInFacet = getCategoriesInFacet(category.get());
            final List<String> selectedCategoryIds = getSelectedCategoryIds(category.get());
            final SearchCriteria searchCriteria = SearchCriteria.of(configuration(), request(), messages, userContext, category.get(), selectedCategoryIds, categoriesInFacet);
            return productService().searchProducts(page, searchCriteria).map(searchResult -> {
                final ProductOverviewPageContent content = createPageContent(userContext, searchResult, page, searchCriteria);
                return ok(renderPage(userContext, fillPageContent(content, userContext, category.get())));
            });
        }
        return F.Promise.pure(notFound("Category not found: " + categorySlug));
    }

    public F.Promise<Result> search(final String languageTag, final int page) {
        final UserContext userContext = userContext(languageTag);
        final Messages messages = messages(userContext);
        final SearchCriteria searchCriteria = SearchCriteria.of(configuration(), request(), messages, userContext, categoryTree());
        if (searchCriteria.searchTerm().isPresent()) {
            final F.Promise<PagedSearchResult<ProductProjection>> searchResultPromise = productService().searchProducts(page, searchCriteria);
            return searchResultPromise.map(searchResult -> {
                final ProductOverviewPageContent content = createPageContent(userContext, searchResult, page, searchCriteria);
                final String searchTerm = searchCriteria.searchTerm().get().getValue();
                return ok(renderPage(userContext, fillPageContent(content, searchTerm)));
            });
        } else {
            return F.Promise.pure(badRequest("Search term missing"));
        }
    }

    /* Methods to render the page */

    private Html renderPage(final UserContext userContext, final ProductOverviewPageContent content) {
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return templateService().renderToHtml("pop", pageData, userContext.locales());
    }

    private ProductOverviewPageContent createPageContent(final UserContext userContext, final PagedSearchResult<ProductProjection> searchResult,
                                                         final int page, final SearchCriteria searchCriteria) {
        final ProductOverviewPageContent content = new ProductOverviewPageContent();
        content.setFilterProductsUrl(request().path());
        content.setProducts(new ProductListData(searchResult.getResults(), productDataConfig(), userContext, reverseRouter(), categoryTreeInNew()));
        content.setPagination(new PaginationData(requestContext(), searchResult, page, searchCriteria.selectedDisplay(), paginationDisplayedPages));
        content.setSortSelector(searchCriteria.boundSortSelector());
        content.setDisplaySelector(searchCriteria.boundDisplaySelector());
        content.setFacets(new FacetListData(searchResult, searchCriteria.boundFacets()));
        return content;
    }

    private ProductOverviewPageContent fillPageContent(final ProductOverviewPageContent pageContent,
                                                       final UserContext userContext, final Category category) {
        pageContent.setAdditionalTitle(category.getName().find(userContext.locales()).orElse(""));
        pageContent.setBreadcrumb(new BreadcrumbData(category, categoryTree(), userContext, reverseRouter()));
        pageContent.setJumbotron(new JumbotronData(category, userContext, categoryTree()));
        pageContent.setBanner(createBanner(userContext, category));
        pageContent.setSeo(new SeoData(userContext, category));
        return pageContent;
    }

    private ProductOverviewPageContent fillPageContent(final ProductOverviewPageContent pageContent,
                                                       final String searchTerm) {
        pageContent.setAdditionalTitle(searchTerm);
        pageContent.setBreadcrumb(new BreadcrumbData(searchTerm));
        pageContent.setSearchTerm(searchTerm);
        return pageContent;
    }

    private static BannerData createBanner(final UserContext userContext, final Category category) {
        final BannerData bannerData = new BannerData(userContext, category);
        bannerData.setImageMobile("/assets/img/banner_mobile.jpg"); // TODO obtain from category?
        bannerData.setImageDesktop("/assets/img/banner_desktop.jpg"); // TODO obtain from category?
        return bannerData;
    }

    private List<String> getSelectedCategoryIds(final Category category) {
        return categoryTree().getSubtree(singletonList(category)).getAllAsFlatList().stream()
                .map(Category::getId)
                .collect(toList());
    }

    private CategoryTree getCategoriesInFacet(final Category category) {
        final List<Category> ancestors = category.getAncestors().stream()
                .map(c -> categoryTree().findById(c.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        final List<Category> siblings = categoryTree().getSiblings(singletonList(category));
        final List<Category> children = categoryTree().findChildren(category);
        final List<Category> relatives = new ArrayList<>();
        relatives.add(category);
        relatives.addAll(ancestors);
        relatives.addAll(siblings);
        relatives.addAll(children);
        return CategoryTree.of(relatives);
    }
}