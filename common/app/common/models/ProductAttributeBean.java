package common.models;

import common.contexts.UserContext;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.MetaProductType;

import static common.utils.ProductAttributeUtils.attributeLabel;
import static common.utils.ProductAttributeUtils.attributeValue;

public class ProductAttributeBean extends Base {
    private String key;
    private String name;
    private String value;

    public ProductAttributeBean() {
    }

    public ProductAttributeBean(final Attribute attribute, final MetaProductType metaProductType, final UserContext userContext) {
        this.key = attribute.getName();
        this.name = attributeLabel(attribute, metaProductType, userContext);
        this.value = attributeValue(attribute, metaProductType, userContext);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }


}
