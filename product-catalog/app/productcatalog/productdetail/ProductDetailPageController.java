package productcatalog.productdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.*;
import productcatalog.productoverview.search.SearchConfig;
import common.suggestion.ProductRecommendation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductDetailPageController extends ProductCatalogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailPageController.class);
    private final int numSuggestions;

    @Inject
    public ProductDetailPageController(final ControllerDependency controllerDependency, final ProductRecommendation productRecommendation,
                                       final ProductDataConfig productDataConfig, final SearchConfig searchConfig) {
        super(controllerDependency, productRecommendation, productDataConfig, searchConfig);
        this.numSuggestions = configuration().getInt("pdp.productSuggestions.count");
    }

    /* Controller actions */

    public CompletionStage<Result> show(final String locale, final String slug, final String sku) {
        final UserContext userContext = userContext(locale);
        return findProductBySlug(userContext.locale(), slug)
                .thenComposeAsync(productOpt -> productOpt
                        .map(product -> renderProduct(slug, sku, product, userContext))
                        .orElseGet(() -> CompletableFuture.completedFuture(notFound())), HttpExecution.defaultContext());
    }

    /**
     * Gets a product, uniquely identified by a locale and a slug
     * @param locale the locale in which you provide the slug
     * @param slug the slug
     * @return A CompletionStage of an optionally found ProductProjection
     */
    protected CompletionStage<Optional<ProductProjection>> findProductBySlug(final Locale locale, final String slug) {
        final ProductProjectionQuery request = ProductProjectionQuery.ofCurrent().bySlug(locale, slug);
        return sphere().execute(request)
                .thenApplyAsync(PagedQueryResult::head, HttpExecution.defaultContext())
                .whenComplete((productOpt, t) -> {
                    if (productOpt.isPresent()) {
                        final String productId = productOpt.get().getId();
                        LOGGER.trace("Found product for slug {} in locale {} with ID {}.", slug, locale, productId);
                    } else {
                        LOGGER.trace("No product found for slug {} in locale {}.", slug, locale);
                    }
                });
    }

    protected CompletionStage<Result> renderProduct(final String slug, final String sku, final ProductProjection product, final UserContext userContext) {
        return product.findVariantBySku(sku)
                .map(variant -> productRecommendation().relatedToProduct(product, numSuggestions).thenApplyAsync(suggestions ->
                        ok(renderProductPage(product, userContext, variant, suggestions)), HttpExecution.defaultContext())
                ).orElseGet(() -> redirectToMasterVariant(userContext, slug, product));
    }

    protected CompletionStage<Result> redirectToMasterVariant(final UserContext userContext, final String slug,
                                                            final ProductProjection product) {
        final String masterVariantSku = product.getMasterVariant().getSku();
        final String languageTag = userContext.locale().toLanguageTag();
        return CompletableFuture.completedFuture(redirect(reverseRouter().showProduct(languageTag, slug, masterVariantSku)));
    }

    /* Page rendering methods */

    protected Html renderProductPage(final ProductProjection product, final UserContext userContext,
                                   final ProductVariant variant, final Set<ProductProjection> suggestions) {
        final ProductDetailPageContent pageContent = createPageContent(userContext, product, variant, suggestions);
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("pdp", pageData, userContext.locales());
    }

    protected ProductDetailPageContent createPageContent(final UserContext userContext, final ProductProjection product,
                                                         final ProductVariant variant, final Set<ProductProjection> suggestions) {
        final ProductDetailPageContent content = new ProductDetailPageContent();
        content.setAdditionalTitle(product.getName().find(userContext.locales()).orElse(""));
        content.setProduct(new ProductBean(product, variant, productDataConfig(), userContext, reverseRouter()));
        content.setBreadcrumb(new BreadcrumbBean(product, variant, categoryTree(), userContext, reverseRouter()));
        content.setShippingRates(createDeliveryData(userContext));
        content.setSuggestions(createSuggestions(userContext, suggestions));
        content.setAddToCartFormUrl(reverseRouter().processAddProductToCartForm(userContext.locale().getLanguage()).url());
        return content;
    }

    protected SuggestionsData createSuggestions(final UserContext userContext, final Set<ProductProjection> suggestions) {
        final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
        return new SuggestionsData(productListData);
    }

    protected List<ShippingRateBean> createDeliveryData(final UserContext userContext) {
        final PriceFormatter priceFormatter = priceFormatter(userContext);
        return getShippingRates().stream()
                .map(rate -> new ShippingRateBean(priceFormatter, rate))
                .collect(toList());
    }

    protected List<ShopShippingRate> getShippingRates() {
        return emptyList(); // TODO get shipping rates for this product
    }
}