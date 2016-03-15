package common.templates.handlebars;

import io.sphere.sdk.models.Base;

import static common.templates.handlebars.HelperUtils.getPart;
import static org.apache.commons.lang3.StringUtils.split;

class CmsIdentifier extends Base {
    private final String contentType;
    private final String contentId;
    private final String field;

    public CmsIdentifier(final String context) {
        final String[] contextParts = split(context, ":", 2);
        this.contentType = getPart(contextParts, 0, "");
        final String content = getPart(contextParts, 1, context);

        final String[] contentParts = split(content, ".", 2);
        this.contentId = getPart(contentParts, 0, "");
        this.field = getPart(contentParts, 1, "");
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
}
