package common.template.engine.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.template.cms.CmsIdentifier;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.util.Optional;

import static common.template.engine.handlebars.HelperUtils.getCmsPageFromContext;

final class HandlebarsCmsHelper extends Base implements Helper<String> {

    public HandlebarsCmsHelper() {
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of(context);
        return getMessage(cmsIdentifier, options).orElse("");
    }

    private Optional<String> getMessage(final CmsIdentifier cmsIdentifier, final Options options) {
        return getCmsPageFromContext(options.context)
                .flatMap(cmsPage -> cmsPage.get(cmsIdentifier.getContentType(), cmsIdentifier.getContentId())
                        .flatMap(cmsContent -> cmsContent.get(cmsIdentifier.getField(), options.hash)));
    }
}
