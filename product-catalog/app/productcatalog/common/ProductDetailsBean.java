package productcatalog.common;

import common.contexts.UserContext;
import common.models.ProductAttributeBean;
import common.models.ProductDataConfig;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductDetailsBean {
    private List<ProductAttributeBean> features;

    public ProductDetailsBean() {
    }

    public ProductDetailsBean(final ProductVariant variant, final ProductDataConfig productDataConfig, final UserContext userContext) {
        this.features = productDataConfig.getDisplayedAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> new ProductAttributeBean(attr, productDataConfig.getMetaProductType(), userContext))
                .collect(toList());
    }

    public List<ProductAttributeBean> getFeatures() {
        return features;
    }

    public void setFeatures(final List<ProductAttributeBean> features) {
        this.features = features;
    }
}
