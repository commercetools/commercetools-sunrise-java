package common.cms;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

/**
 * Obtains content data for a page, usually coming from a Content Management System (CMS).
 */
@FunctionalInterface
public interface CmsPage {

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param messageKey identifying the message
     * @param hashArgs list of hash arguments
     * @return the message identified by the key, with the given arguments, or absent if not found
     */
    Optional<String> get(final String messageKey, final Map<String, Object> hashArgs);

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param messageKey identifying the message
     * @return the message identified by the key, or absent if not found
     */
    default Optional<String> get(final String messageKey) {
        return get(messageKey, emptyMap());
    }

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param messageKey identifying the message
     * @param hashArgs list of hash arguments
     * @return the message identified by the key, with the given arguments, or empty string if not found
     */
    default String getOrEmpty(final String messageKey, final Map<String, Object> hashArgs) {
        return get(messageKey, hashArgs).orElse("");
    }

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param messageKey identifying the message
     * @return the message identified by the key, or empty string if not found
     */
    default String getOrEmpty(final String messageKey) {
        return get(messageKey).orElse("");
    }
}
