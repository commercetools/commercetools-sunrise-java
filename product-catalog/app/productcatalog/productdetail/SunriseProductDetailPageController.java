package productcatalog.productdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.SunrisePageData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import wedecidelatercommon.ProductReverseRouter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseProductDetailPageController extends SunriseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductDetailPageController.class);

    @Inject
    private UserContext userContext;
    @Inject
    private ProductReverseRouter productReverseRouter;
    @Inject
    private ProductFetchBySlugAndSku productFetchBySlugAndSku;
    @Inject
    protected ProductDetailPageContentFactory productDetailPageContentFactory;

    @Nullable
    private String productSlug;
    @Nullable
    private String variantSku;

    @Inject
    public SunriseProductDetailPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public CompletionStage<Result> showProductBySlugAndSku(final String languageTag, final String slug, final String sku) {
        this.productSlug = slug;
        this.variantSku = sku;
        return productFetchBySlugAndSku.findProduct(slug, sku)
                .thenComposeAsync(this::showProduct, HttpExecution.defaultContext());
    }

    protected Optional<String> getProductSlug() {
        return Optional.ofNullable(productSlug);
    }

    protected Optional<String> getVariantSku() {
        return Optional.ofNullable(variantSku);
    }

    protected CompletionStage<Result> showProduct(final ProductFetchResult productFetchResult) {
        final Optional<ProductProjection> product = productFetchResult.getProduct();
        final Optional<ProductVariant> variant = productFetchResult.getVariant();
        if (product.isPresent() && variant.isPresent()) {
            return handleFoundProduct(product.get(), variant.get());
        } else if (product.isPresent()) {
            return handleNotFoundVariant(product.get());
        } else {
            return handleNotFoundProduct();
        }
    }

    protected CompletionStage<Result> handleFoundProduct(final ProductProjection product, final ProductVariant variant) {
        final ProductDetailPageContent pageContent = createPageContent(product, variant);
        return completedFuture(ok(renderPage(pageContent)));
    }

    protected CompletionStage<Result> handleNotFoundVariant(final ProductProjection product) {
        return redirectToMasterVariant(product);
    }

    protected CompletionStage<Result> handleNotFoundProduct() {
        if (getProductSlug().isPresent() && getVariantSku().isPresent()) {
            return findNewProductSlug(getProductSlug().get()).thenApplyAsync(newSlugOpt -> newSlugOpt
                    .map(newSlug -> redirectToNewSlug(newSlug, getVariantSku().get()))
                    .orElseGet(this::notFoundProductResult),
                    HttpExecution.defaultContext());
        } else {
            return completedFuture(notFoundProductResult());
        }
    }

    protected ProductDetailPageContent createPageContent(final ProductProjection product, final ProductVariant variant) {
        return productDetailPageContentFactory.create(product, variant);
    }

    protected Html renderPage(final ProductDetailPageContent pageContent) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("pdp", pageData, userContext.locales());
    }

    private Result notFoundProductResult() {
        return notFound();
    }

    private Result redirectToNewSlug(final String newSlug, final String sku) {
        return movedPermanently(productReverseRouter.productDetailPageCall(userContext.locale().toLanguageTag(), newSlug, sku));
    }

    private CompletionStage<Result> redirectToMasterVariant(final ProductProjection product) {
        return reverseRouter().productDetailPageCall(userContext.locale(), product, product.getMasterVariant())
                .map(call -> completedFuture(redirect(call)))
                .orElseGet(() -> completedFuture(notFoundProductResult()));
    }

    private CompletionStage<Optional<String>> findNewProductSlug(final String slug) {
        return completedFuture(Optional.empty()); // TODO look for messages and find current slug
    }
}