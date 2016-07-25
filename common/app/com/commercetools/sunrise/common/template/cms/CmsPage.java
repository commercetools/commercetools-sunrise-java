package com.commercetools.sunrise.common.template.cms;

import java.util.Optional;

public interface CmsPage {

    /**
     * Gets the content corresponding to the given field name.
     * @param fieldName identifying the field (e.g. banner.image.url)
     * @return the content identified by the key, or absent if not found
     */
    Optional<String> get(final String fieldName);

    /**
     * Gets the content corresponding to the given field name.
     * @param fieldName identifying the field (e.g. banner.image.url)
     * @return the content identified by the key, or empty string if not found
     */
    default String getOrEmpty(final String fieldName) {
        return get(fieldName).orElse("");
    }
}
