package com.commercetools.sunrise.common.sessions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestableObjectStoringSessionStrategy implements ObjectStoringSessionStrategy {

    private final Map<String, Object> session = new HashMap<>();

    @Override
    public Optional<String> findValueByKey(final String key) {
        return findObjectByKey(key, String.class);
    }

    @Override
    public void overwriteValueByKey(final String key, final String value) {
        overwriteObjectByKey(key, value);
    }

    @Override
    public void removeValueByKey(final String key) {
        removeObjectByKey(key);
    }

    @Override
    public <U> Optional<U> findObjectByKey(final String key, final Class<U> clazz) {
        return Optional.ofNullable((U) session.get(key));
    }

    @Override
    public <U> void overwriteObjectByKey(final String key, final U object) {
        session.put(key, object);
    }

    @Override
    public void removeObjectByKey(final String key) {
        session.remove(key);
    }
}
