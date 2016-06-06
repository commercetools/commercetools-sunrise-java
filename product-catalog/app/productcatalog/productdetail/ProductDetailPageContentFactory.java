package productcatalog.productdetail;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import productcatalog.common.BreadcrumbBeanFactory;
import productcatalog.common.ProductBeanFactory;

import javax.inject.Inject;

public class ProductDetailPageContentFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private ReverseRouter reverseRouter;
    @Inject
    private BreadcrumbBeanFactory breadcrumbBeanFactory;
    @Inject
    private ProductBeanFactory productBeanFactory;

    public ProductDetailPageContent create(final ProductProjection product, final ProductVariant variant) {
        final ProductDetailPageContent content = new ProductDetailPageContent();
        content.setAdditionalTitle(getAdditionalTitle(product));
        content.setProduct(productBeanFactory.create(product, variant));
        content.setBreadcrumb(breadcrumbBeanFactory.create(product, variant));
        content.setAddToCartFormUrl(getAddToCartUrl()); // TODO move to page meta
        return content;
    }

    private String getAdditionalTitle(final ProductProjection product) {
        return product.getName().find(userContext.locales()).orElse("");
    }

    private String getAddToCartUrl() {
        return reverseRouter.processAddProductToCartForm(userContext.locale().getLanguage()).url();
    }
}
