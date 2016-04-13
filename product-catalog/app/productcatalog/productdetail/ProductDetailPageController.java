package productcatalog.productdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.*;
import productcatalog.productoverview.search.SearchConfig;
import productcatalog.services.ProductService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductDetailPageController extends ProductCatalogController {
    private final int numSuggestions;

    @Inject
    public ProductDetailPageController(final ControllerDependency controllerDependency, final ProductService productService,
                                       final ProductDataConfig productDataConfig, final SearchConfig searchConfig) {
        super(controllerDependency, productService, productDataConfig, searchConfig);
        this.numSuggestions = configuration().getInt("pdp.productSuggestions.count");
    }

    /* Controller actions */

    public CompletionStage<Result> show(final String locale, final String slug, final String sku) {
        final UserContext userContext = userContext(locale);
        return productService().findProductBySlug(userContext.locale(), slug)
                .thenComposeAsync(productOpt -> productOpt
                        .map(product -> renderProduct(slug, sku, product, userContext))
                        .orElseGet(() -> CompletableFuture.completedFuture(notFound())), HttpExecution.defaultContext());
    }

    private CompletionStage<Result> renderProduct(final String slug, final String sku, final ProductProjection product, final UserContext userContext) {
        return product.findVariantBySku(sku)
                .map(variant -> productService().getSuggestions(product, categoryTree(), numSuggestions).thenApplyAsync(suggestions ->
                        ok(renderProductPage(product, userContext, variant, suggestions)), HttpExecution.defaultContext())
                ).orElseGet(() -> redirectToMasterVariant(userContext, slug, product));
    }

    private CompletionStage<Result> redirectToMasterVariant(final UserContext userContext, final String slug,
                                                            final ProductProjection product) {
        final String masterVariantSku = product.getMasterVariant().getSku();
        final String languageTag = userContext.locale().toLanguageTag();
        return CompletableFuture.completedFuture(redirect(reverseRouter().showProduct(languageTag, slug, masterVariantSku)));
    }

    /* Methods to render the page */

    private Html renderProductPage(final ProductProjection product, final UserContext userContext,
                                   final ProductVariant variant, final List<ProductProjection> suggestions) {
        final ProductDetailPageContent pageContent = createPageContent(userContext, product, variant, suggestions);
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("pdp", pageData, userContext.locales());
    }

    private ProductDetailPageContent createPageContent(final UserContext userContext, final ProductProjection product,
                                                       final ProductVariant variant, final List<ProductProjection> suggestions) {
        final ProductDetailPageContent content = new ProductDetailPageContent();
        content.setAdditionalTitle(product.getName().find(userContext.locales()).orElse(""));
        content.setProduct(new ProductBean(product, variant, productDataConfig(), userContext, reverseRouter()));
        content.setBreadcrumb(new BreadcrumbBean(product, variant, categoryTree(), userContext, reverseRouter()));
        content.setShippingRates(createDeliveryData(userContext));
        content.setSuggestions(createSuggestions(userContext, suggestions));
        content.setAddToCartFormUrl(reverseRouter().processAddProductToCartForm(userContext.locale().getLanguage()).url());
        return content;
    }

    private SuggestionsData createSuggestions(final UserContext userContext, final List<ProductProjection> suggestions) {
        final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
        return new SuggestionsData(productListData);
    }

    private List<ShippingRateBean> createDeliveryData(final UserContext userContext) {
        final PriceFormatter priceFormatter = priceFormatter(userContext);
        return getShippingRates().stream()
                .map(rate -> new ShippingRateBean(priceFormatter, rate))
                .collect(toList());
    }

    private List<ShopShippingRate> getShippingRates() {
        return emptyList(); // TODO get shipping rates for this product
    }
}