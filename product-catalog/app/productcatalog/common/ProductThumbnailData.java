package productcatalog.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public class ProductThumbnailData extends Base {
    private boolean sale;
    private boolean _new;
    private ProductData product;

    public ProductThumbnailData() {
    }

    public ProductThumbnailData(final ProductProjection product, final ProductVariant variant, final ProductDataConfig productDataConfig,
                                final UserContext userContext, final ReverseRouter reverseRouter, final CategoryTree categoryTreeNew) {
        this.sale = calculateIsSale(this.product);
        this._new = product.getCategories().stream()
                .anyMatch(category -> categoryTreeNew.findById(category.getId()).isPresent());
        this.product = new ProductData(product, variant, productDataConfig, userContext, reverseRouter);
    }

    private static boolean calculateIsSale(final ProductData productData) {
        return productData != null && productData.getVariant() != null && productData.getVariant().getPriceOld() != null;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(final boolean sale) {
        this.sale = sale;
    }

    public boolean isNew() {
        return _new;
    }

    public void setNew(final boolean _new) {
        this._new = _new;
    }

    public ProductData getProduct() {
        return product;
    }

    public void setProduct(final ProductData product) {
        this.product = product;
    }
}
