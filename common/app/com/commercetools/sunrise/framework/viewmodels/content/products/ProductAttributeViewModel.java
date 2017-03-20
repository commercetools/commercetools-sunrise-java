package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import io.sphere.sdk.models.LocalizedString;

public class ProductAttributeViewModel extends ViewModel {

    private String key;
    private LocalizedString name;
    private String value;

    public ProductAttributeViewModel() {
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
