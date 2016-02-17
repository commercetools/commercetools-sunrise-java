package common.models;

import io.sphere.sdk.producttypes.MetaProductType;

import java.util.ArrayList;
import java.util.List;

public class ProductDataConfig {
    private final MetaProductType metaProductType;
    private final List<String> displayedAttributes;
    private final List<String> softSelectableAttributes;
    private final List<String> hardSelectableAttributes;
    private final List<String> selectableAttributes;

    private ProductDataConfig(final MetaProductType metaProductType, final List<String> displayedAttributes,
                              final List<String> softSelectableAttributes, final List<String> hardSelectableAttributes) {
        this.metaProductType = metaProductType;
        this.displayedAttributes = displayedAttributes;
        this.softSelectableAttributes = softSelectableAttributes;
        this.hardSelectableAttributes = hardSelectableAttributes;
        this.selectableAttributes = allSelectableAttributes(softSelectableAttributes, hardSelectableAttributes);
    }

    public MetaProductType getMetaProductType() {
        return metaProductType;
    }

    public List<String> getDisplayedAttributes() {
        return displayedAttributes;
    }

    public List<String> getSoftSelectableAttributes() {
        return softSelectableAttributes;
    }

    public List<String> getHardSelectableAttributes() {
        return hardSelectableAttributes;
    }

    public List<String> getSelectableAttributes() {
        return selectableAttributes;
    }

    public static ProductDataConfig of(final MetaProductType metaProductType, final List<String> displayedAttributes,
                                       final List<String> selectableAttributes, final List<String> hardSelectableAttributes) {
        return new ProductDataConfig(metaProductType, displayedAttributes, selectableAttributes, hardSelectableAttributes);
    }

    private static List<String> allSelectableAttributes(final List<String> softSelectableAttributes,
                                                        final List<String> hardSelectableAttributes) {
        final List<String> selectableAttributes = new ArrayList<>(hardSelectableAttributes);
        selectableAttributes.addAll(softSelectableAttributes);
        return selectableAttributes;
    }
}

