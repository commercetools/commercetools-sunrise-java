package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.*;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.ProductNotFoundException;
import productcatalog.models.ProductVariantNotFoundException;
import productcatalog.models.ShopShippingRate;
import productcatalog.pages.*;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;
import productcatalog.services.ShippingMethodService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Locale.GERMAN;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductDetailPageController extends SunriseController {

    private final int numberOfSuggestions;
    private final ProductProjectionService productService;
    private final CategoryService categoryService;
    private final ShippingMethodService shippingMethodService;

    @Inject
    public ProductDetailPageController(final Configuration configuration, final ControllerDependency controllerDependency, final ProductProjectionService productService, final CategoryService categoryService, final ShippingMethodService shippingMethodService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.shippingMethodService = shippingMethodService;
        this.numberOfSuggestions = configuration.getInt("pdp.productSuggestions.count");
    }

    private List<ProductThumbnailData> suggestionsToViewData(final List<ProductProjection> suggestions) {
        return suggestions.stream().map((product) -> ProductThumbnailDataFactory.of(getTranslator(),  getPriceFinder(), getPriceFormatter()).create(product)).collect(toList());
    }



    public F.Promise<Result> pdp(final String slug, final String sku) {
        //step 1 get all parameters from header/url
        final Locale locale = GERMAN;//TODO get from URL

        //step 2 create a chain/parallelize external calls
        final F.Promise<ProductProjection> productProjectionPromise = fetchProduct(slug, locale);
        final F.Promise<List<ProductProjection>> suggestionPromise = productProjectionPromise.flatMap(productProjection -> productService.getSuggestions(categoryService.getSiblingCategories(productProjection.getCategories()), numberOfSuggestions));
        final F.Promise<CmsPage> cmsPagePromise = getPage("pdp");

        //step 3 call method that does not need to call external systems
        final F.Promise<Result> resultPromise = productProjectionPromise.flatMap(productProjection -> {
            return cmsPagePromise.flatMap(cms -> {
                return suggestionPromise.flatMap(suggestions -> {
                    return getPdpResult(cms, suggestions, productProjection, sku);
                });
            });
        });

        //step 4 recover centrally for exceptions
        return resultPromise.recover(exception -> {
            if (exception instanceof ProductNotFoundException || exception instanceof ProductVariantNotFoundException) {
                return notFoundAction();
            } else {
                throw exception;
            }
        });
    }

    //TODO the promise needs to be removed
    private F.Promise<Result> getPdpResult(final CmsPage cms, final List<ProductProjection> suggestions, final ProductProjection productProjection, final String sku) {
        final Translator translator = getTranslator();
        final PriceFormatter priceFormatter = getPriceFormatter();
        final PriceFinder priceFinder = getPriceFinder();

        final ProductVariant productVariant = obtainProductVariantBySku(sku, productProjection);
        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(priceFormatter);
        final CategoryLinkDataFactory categoryLinkDataFactory = CategoryLinkDataFactory.of(translator);

        final String additionalTitle = translator.findTranslation(productProjection.getName());
        final PdpStaticData staticData = new PdpStaticData(cms, BagItemDataFactory.of().create(100), RatingDataFactory.of(cms).create());
        final List<Category> breadcrumbs = getBreadcrumbsForProduct(productProjection);
        final List<LinkData> breadcrumbData = breadcrumbs.stream().map(categoryLinkDataFactory::create).collect(toList());
        final List<ImageData> galleryData = productVariant.getImages().stream().map(ImageData::of).collect(toList());
        final ProductData productData = ProductDataFactory.of(translator, priceFinder, priceFormatter).create(productProjection, productVariant);
        final List<ShippingRateData> deliveryData = getShippingRates().stream().map(shippingRateDataFactory::create).collect(toList());
        final List<ProductThumbnailData> suggestionData = suggestionsToViewData(suggestions);

        final ProductDetailPageContent content = new ProductDetailPageContent(additionalTitle, staticData, breadcrumbData, galleryData, productData, deliveryData, suggestionData);

        return render(view -> ok(view.productDetailPage(content)));
    }

    private PriceFinder getPriceFinder() {
        return context().user().priceFinder();
    }

    private PriceFormatter getPriceFormatter() {
        return context().user().priceFormatter();
    }

    private Translator getTranslator() {
        return context().translator();
    }

    private ProductVariant obtainProductVariantBySku(final String sku, final ProductProjection productProjection) {
        final Optional<ProductVariant> variantOptional = productService.findVariantBySku(productProjection, sku);
        if (variantOptional.isPresent()) {
            return variantOptional.get();
        } else {
            throw ProductVariantNotFoundException.bySku(sku);
        }
    }

    private Result notFoundAction() {
        return notFound();
    }

    private F.Promise<ProductProjection> fetchProduct(final String slug, final Locale locale) {
        return productService.searchProductBySlug(locale, slug).map(productOptional -> {
            if (productOptional.isPresent()) {
                return productOptional.get();
            } else {
                throw ProductNotFoundException.bySlug(locale, slug);
            }
        });
    }

    private List<ShopShippingRate> getShippingRates() {
        return shippingMethodService.getShippingRates(context().user().zone());
    }

    private List<Category> getBreadcrumbsForProduct(final ProductProjection product) {
        return product.getCategories().stream().findFirst()
                    .map(categoryService::getBreadCrumbCategories)
                    .orElse(Collections.<Category>emptyList());
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}