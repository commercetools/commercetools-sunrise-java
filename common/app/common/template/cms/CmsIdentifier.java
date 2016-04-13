package common.template.cms;

import io.sphere.sdk.models.Base;

import static common.utils.SunriseArrayUtils.getArrayElement;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * CMS Identifier, consisting of the field name and the content type and ID.
 * @see CmsService
 */
public class CmsIdentifier extends Base {
    private final String contentType;
    private final String contentId;
    private final String field;

    private CmsIdentifier(final String contentType, final String contentId, final String field) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.field = field;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public String getField() {
        return field;
    }

    /**
     * Creates a CMS Identifier, consisting of the field name and the content type and ID.
     * @param fieldWithContent of the form {@code contentType:contentId.field}
     * @return the CMS Identifier for the given input
     */
    public static CmsIdentifier of(final String fieldWithContent) {
        final String[] parts = split(fieldWithContent, ":", 2);
        final String contentType = getArrayElement(parts, 0, "");
        final String content = getArrayElement(parts, 1, fieldWithContent);

        final String[] contentParts = split(content, ".", 2);
        final String contentId = getArrayElement(contentParts, 0, "");
        final String field = getArrayElement(contentParts, 1, "");

        return ofContentTypeAndIdAndField(contentType, contentId, field);
    }

    public static CmsIdentifier ofContentTypeAndIdAndField(final String contentType, final String contentId, final String field) {
        return new CmsIdentifier(contentType, contentId, field);
    }
}
