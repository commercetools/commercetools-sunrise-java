package productcatalog.productdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.ReverseRouter;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.BreadcrumbBean;
import productcatalog.common.ProductBean;
import productcatalog.common.ProductCatalogController;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseProductDetailPageController extends ProductCatalogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductDetailPageController.class);

    @Inject
    private UserContext userContext;
    @Inject
    private ReverseRouter reverseRouter;
    @Inject
    private ProductDataConfig productDataConfig;
    @Inject
    private ProductFetchBySlugAndSku productFetchBySlugAndSku;

    @Nullable
    private String slug;
    @Nullable
    private String sku;

    @Inject
    public SunriseProductDetailPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public CompletionStage<Result> showProductBySlugAndSku(final String languageTag, final String slug, final String sku) {
        this.slug = slug;
        this.sku = sku;
        return productFetchBySlugAndSku.findProduct(slug, sku)
                .thenComposeAsync(this::showProduct, HttpExecution.defaultContext());
    }

    protected Optional<String> getSlug() {
        return Optional.ofNullable(slug);
    }

    protected Optional<String> getSku() {
        return Optional.ofNullable(sku);
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
        if (getSlug().isPresent() && getSku().isPresent()) {
            return findNewProductSlug(getSlug().get()).thenApplyAsync(newSlugOpt -> newSlugOpt
                    .map(newSlug -> redirectToNewSlug(newSlug, getSku().get()))
                    .orElseGet(this::notFoundProductResult),
                    HttpExecution.defaultContext());
        } else {
            return completedFuture(notFoundProductResult());
        }
    }

    protected ProductDetailPageContent createPageContent(final ProductProjection product, final ProductVariant variant) {
        final ProductDetailPageContent content = new ProductDetailPageContent();
        content.setAdditionalTitle(product.getName().find(userContext.locales()).orElse(""));
        content.setProduct(new ProductBean(product, variant, productDataConfig, userContext, reverseRouter()));
        content.setBreadcrumb(new BreadcrumbBean(product, variant, categoryTree(), userContext, reverseRouter()));
        content.setAddToCartFormUrl(reverseRouter().processAddProductToCartForm(userContext.locale().getLanguage()).url()); // TODO move to page meta
        return content;
    }

    protected Html renderPage(final ProductDetailPageContent pageContent) {
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("pdp", pageData, userContext.locales());
    }

    private Result notFoundProductResult() {
        return notFound();
    }

    private Result redirectToNewSlug(final String newSlug, final String sku) {
        return movedPermanently(reverseRouter.showProduct(userContext.locale().toLanguageTag(), newSlug, sku));
    }

    private CompletionStage<Result> redirectToMasterVariant(final ProductProjection product) {
        return reverseRouter().showProduct(userContext.locale(), product, product.getMasterVariant())
                .map(call -> completedFuture(redirect(call)))
                .orElseGet(() -> completedFuture(notFoundProductResult()));
    }

    private CompletionStage<Optional<String>> findNewProductSlug(final String slug) {
        return completedFuture(Optional.empty()); // TODO look for messages and find current slug
    }
}