package productcatalog.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public class ProductVariantReferenceBean extends Base {
    private int id;
    private String url;

    public ProductVariantReferenceBean() {
    }

    public ProductVariantReferenceBean(final ProductVariant productVariant, final ProductProjection product,
                                       final UserContext userContext, final ReverseRouter reverseRouter) {
        this.id = productVariant.getId();
        this.url = reverseRouter.showProductUrlOrEmpty(userContext.locale(), product, productVariant);
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
