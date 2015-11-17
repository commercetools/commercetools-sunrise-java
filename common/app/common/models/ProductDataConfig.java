package common.models;

import io.sphere.sdk.producttypes.MetaProductType;

import java.util.List;

public class ProductDataConfig {

    private final MetaProductType metaProductType;
    private final List<String> attributesWhitelist;

    public ProductDataConfig(final MetaProductType metaProductType, final List<String> attributesWhitelist) {
        this.metaProductType = metaProductType;
        this.attributesWhitelist = attributesWhitelist;
    }

    public List<String> getAttributeWhiteList() {
        return attributesWhitelist;
    }

    public MetaProductType getMetaProductType() {
        return metaProductType;
    }
}

