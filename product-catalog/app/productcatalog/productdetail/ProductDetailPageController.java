package productcatalog.productdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.F;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.*;
import productcatalog.services.ProductService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductDetailPageController extends ProductCatalogController {
    private final int numSuggestions;

    @Inject
    public ProductDetailPageController(final ControllerDependency controllerDependency,
                                       final ProductService productService, final ProductDataConfig productDataConfig) {
        super(controllerDependency, productService, productDataConfig);
        this.numSuggestions = configuration().getInt("pdp.productSuggestions.count");
    }

    /* Controller actions */

    public F.Promise<Result> show(final String locale, final String slug, final String sku) {
        final UserContext userContext = userContext(locale);
        return productService().findProductBySlug(userContext.locale(), slug)
                .flatMap(productOpt -> productOpt
                        .map(product -> renderProduct(userContext, slug, product, sku))
                        .orElseGet(() -> F.Promise.pure(notFound())));
    }

    private F.Promise<Result> renderProduct(final UserContext userContext, final String slug, final ProductProjection product, final String sku) {
        return product.findVariantBySku(sku)
                .map(variant ->
                    productService().getSuggestions(product, categoryTree(), numSuggestions).map(suggestions -> {
                        final ProductDetailPageContent content = createPageContent(userContext, product, variant, suggestions);
                        return (Result) ok(renderPage(userContext, content));
                    })
                ).orElseGet(() -> redirectToMasterVariant(userContext, slug, product));
    }

    private F.Promise<Result> redirectToMasterVariant(final UserContext userContext, final String slug,
                                                      final ProductProjection product) {
        final String masterVariantSku = product.getMasterVariant().getSku();
        final String languageTag = userContext.locale().toLanguageTag();
        return F.Promise.pure(redirect(reverseRouter().product(languageTag, slug, masterVariantSku)));
    }

    /* Methods to render the page */

    private Html renderPage(final UserContext userContext, final ProductDetailPageContent content) {
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return templateService().renderToHtml("pdp", pageData, userContext.locales());
    }

    private ProductDetailPageContent createPageContent(final UserContext userContext, final ProductProjection product,
                                                       final ProductVariant variant, final List<ProductProjection> suggestions) {
        final ProductDetailPageContent content = new ProductDetailPageContent();
        content.setAdditionalTitle(product.getName().find(userContext.locales()).orElse(""));
        content.setProduct(new ProductData(product, variant, productDataConfig(), userContext, reverseRouter()));
        content.setBreadcrumb(new BreadcrumbData(product, variant, categoryTree(), userContext, reverseRouter()));
        content.setShippingRates(createDeliveryData(userContext));
        content.setSuggestions(createSuggestions(userContext, suggestions));
        content.setAddToCartFormUrl(reverseRouter().productToCartForm(userContext.locale().getLanguage()).url());
        return content;
    }

    private SuggestionsData createSuggestions(final UserContext userContext, final List<ProductProjection> suggestions) {
        final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
        return new SuggestionsData(productListData);
    }

    private List<ShippingRateData> createDeliveryData(final UserContext userContext) {
        final PriceFormatter priceFormatter = priceFormatter(userContext);
        return getShippingRates().stream()
                .map(rate -> new ShippingRateData(priceFormatter, rate))
                .collect(toList());
    }

    private List<ShopShippingRate> getShippingRates() {
        return emptyList(); // TODO get shipping rates for this product
    }
}