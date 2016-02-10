package common.models;

import io.sphere.sdk.producttypes.MetaProductType;

import java.util.List;

public class ProductDataConfig {
    private final MetaProductType metaProductType;
    private final List<String> displayableAttributes;
    private final List<String> selectableAttributes;

    private ProductDataConfig(final MetaProductType metaProductType, final List<String> displayedAttributes,
                              final List<String> selectableAttributes) {
        this.metaProductType = metaProductType;
        this.selectableAttributes = selectableAttributes;
        this.displayableAttributes = displayedAttributes;
    }

    public MetaProductType getMetaProductType() {
        return metaProductType;
    }

    public List<String> getDisplayedAttributes() {
        return displayableAttributes;
    }

    public List<String> getSelectableAttributes() {
        return selectableAttributes;
    }

    public static ProductDataConfig of(final MetaProductType metaProductType, final List<String> displayedAttributes,
                                       final List<String> selectableAttributes) {
        return new ProductDataConfig(metaProductType, displayedAttributes, selectableAttributes);
    }
}

