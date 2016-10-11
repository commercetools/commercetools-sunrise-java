package com.commercetools.sunrise.common.models;

public class ProductAttributeBean extends ViewModel {

    private String key;
    private String name;
    private String value;

    public ProductAttributeBean() {
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
