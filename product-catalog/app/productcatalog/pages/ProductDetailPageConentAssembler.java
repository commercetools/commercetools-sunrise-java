package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.ImageData;
import common.pages.LinkData;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductDetailPageConentAssembler {

    private final CmsPage cms;
    private final Translator translator;

    private final ProductListDataAssembler productListDataAssembler;
    private final ProductDataAssembler productDataAssembler;

    private ProductDetailPageConentAssembler(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.cms = cms;
        this.translator = translator;

        productListDataAssembler = ProductListDataAssembler.of(cms, translator, priceFinder, priceFormatter);
        productDataAssembler = ProductDataAssembler.of(cms, translator, priceFinder, priceFormatter);
    }

    public static ProductDetailPageConentAssembler of(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductDetailPageConentAssembler(cms, translator, priceFinder, priceFormatter);
    }

    public ProductDetailPageContent assemblePdpContent(final ProductProjection product, ProductVariant variant, final List<ProductProjection> suggestions, final List<ShippingMethod> shippingMethods, final List<Category> breadCrumbCategories) {
        return new ProductDetailPageContent(
                cms,
                translator.translate(product.getName()),
                cms.getOrEmpty("content.text"),
                assembleBreadCrumb(breadCrumbCategories),
                assembleGallery(variant),
                productDataAssembler.assembleProduct(product, product.getMasterVariant(), shippingMethods),
                assembleSuggestions(suggestions)
        );
    }

    private List<LinkData> assembleBreadCrumb(final List<Category> breadcrumbCategories) {
        return breadcrumbCategories.stream().map(this::categoryToLinkData).collect(toList());
    }

    private List<ImageData> assembleGallery(final ProductVariant variant) {
        return variant.getImages().stream().map(ImageData::of).collect(toList());
    }

    private ProductListData assembleSuggestions(final List<ProductProjection> suggestions) {
        return productListDataAssembler.assembleProductListData(
                cms.getOrEmpty("suggestions.text"),
                cms.getOrEmpty("suggestions.sale"),
                cms.getOrEmpty("suggestions.new"),
                cms.getOrEmpty("suggestions.quickView"),
                cms.getOrEmpty("suggestions.wishlist"),
                cms.getOrEmpty("suggestions.moreColors"),
                suggestions
        );
    }

    private LinkData categoryToLinkData(final Category category) {
        final String label = translator.translate(category.getName());
        return new LinkData(label, "");
    }
}
