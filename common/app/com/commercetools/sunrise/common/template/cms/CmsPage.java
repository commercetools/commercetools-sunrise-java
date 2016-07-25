package com.commercetools.sunrise.common.template.cms;

import java.util.Optional;

public interface CmsPage {

    /**
     * Gets the content corresponding to the given field name.
     * @param fieldName identifying the field (e.g. banner.image.url)
     * @return the content identified by the key, or absent if not found
     */
    Optional<String> field(final String fieldName);

    /**
     * Gets the content corresponding to the given field name.
     * @param fieldName identifying the field (e.g. banner.image.url)
     * @return the content identified by the key, or empty string if not found
     */
    default String fieldOrEmpty(final String fieldName) {
        return field(fieldName).orElse("");
    }
}
