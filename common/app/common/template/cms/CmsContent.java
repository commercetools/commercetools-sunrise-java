package common.template.cms;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

/**
 * Obtains fields from a CMS content.
 *
 * @see CmsService
 */
@FunctionalInterface
public interface CmsContent {

    /**
     * Gets the message corresponding to the given field, or empty in case it does not exist.
     * @param field the desired field from the content
     * @param hashArgs list of hash arguments
     * @return the message for the field, with the given arguments, or absent if not found
     */
    Optional<String> get(final String field, final Map<String, Object> hashArgs);

    /**
     * Gets the message corresponding to the given field, or empty in case it does not exist.
     * @param field the desired field from the content
     * @return the message for the field, or absent if not found
     */
    default Optional<String> get(final String field) {
        return get(field, emptyMap());
    }

    /**
     * Gets the message corresponding to the given field, or empty string in case it does not exist.
     * @param field the desired field from the content
     * @param hashArgs list of hash arguments
     * @return the message for the field, with the given arguments, or empty string if not found
     */
    default String getOrEmpty(final String field, final Map<String, Object> hashArgs) {
        return get(field, hashArgs).orElse("");
    }

    /**
     * Gets the message corresponding to the given field, or empty string in case it does not exist.
     * @param field the desired field from the content
     * @return the message for the field, with the given arguments, or empty string if not found
     */
    default String getOrEmpty(final String field) {
        return get(field).orElse("");
    }
}
