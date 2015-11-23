package productcatalog.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import common.models.ProductVariantBean;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public class ProductData extends Base {
    private String url;
    // TODO ratingX
    private boolean sale;
    private boolean _new;
    private boolean moreColors;
    private ProductVariantBean data;

    public ProductData() {
    }

    public ProductData(final ProductProjection product, final ProductVariant variant, final ProductDataConfig productDataConfig,
                       final UserContext userContext, final ReverseRouter reverseRouter, final CategoryTree categoryTreeNew) {
        final String slug = product.getSlug().find(userContext.locale()).orElse("");
        this.url = reverseRouter.product(userContext.locale().toLanguageTag(), slug, variant.getSku()).url();
        this._new = product.getCategories().stream()
                .anyMatch(category -> categoryTreeNew.findById(category.getId()).isPresent());
        //this.sale = TODO get from variant if there is old price
        //this.moreColors = TODO get distinct from variant
        this.data = new ProductVariantBean(product, variant, userContext, productDataConfig);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
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

    public boolean isMoreColors() {
        return moreColors;
    }

    public void setMoreColors(final boolean moreColors) {
        this.moreColors = moreColors;
    }

    public ProductVariantBean getData() {
        return data;
    }

    public void setData(final ProductVariantBean data) {
        this.data = data;
    }
}
