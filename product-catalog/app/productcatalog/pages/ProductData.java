package productcatalog.pages;

import common.contexts.UserContext;
import common.pages.ReverseRouter;
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

    public ProductData() {
    }

    public ProductData(final UserContext userContext, final ReverseRouter reverseRouter, final CategoryTree categoryTreeNew,
                       final ProductProjection product, final ProductVariant variant) {
        final String slug = product.getSlug().find(userContext.locale()).orElse("");
        this.url = reverseRouter.product(userContext.locale().toLanguageTag(), slug, variant.getSku()).url();
        this._new = product.getCategories().stream()
                .anyMatch(category -> categoryTreeNew.findById(category.getId()).isPresent());
        //this.sale = TODO get from variant if there is old price
        //this.moreColors = TODO get distinct from variant
    }


}
