package com.commercetools.sunrise.common.models.products;

import com.commercetools.sunrise.common.models.ViewModel;
import io.sphere.sdk.models.LocalizedString;

public class ProductAttributeBean extends ViewModel {

    private String key;
    private LocalizedString name;
    private String value;

    public ProductAttributeBean() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public LocalizedString getName() {
        return name;
    }

    public void setName(final LocalizedString name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }


}
