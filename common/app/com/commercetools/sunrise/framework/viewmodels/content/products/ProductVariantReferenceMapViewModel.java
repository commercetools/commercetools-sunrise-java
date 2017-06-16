package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProductVariantReferenceMapViewModel extends ViewModel implements Map<String, ProductVariantReferenceViewModel> {

    private final Map<String, ProductVariantReferenceViewModel> delegate = new HashMap<>();

    public ProductVariantReferenceMapViewModel() {
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public ProductVariantReferenceViewModel get(final Object key) {
        return delegate.get(key);
    }

    @Override
    public ProductVariantReferenceViewModel put(final String key, final ProductVariantReferenceViewModel value) {
        return delegate.put(key, value);
    }

    @Override
    public ProductVariantReferenceViewModel remove(final Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends ProductVariantReferenceViewModel> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    @NotNull
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    @NotNull
    public Collection<ProductVariantReferenceViewModel> values() {
        return delegate.values();
    }

    @Override
    @NotNull
    public Set<Map.Entry<String, ProductVariantReferenceViewModel>> entrySet() {
        return delegate.entrySet();
    }
}
