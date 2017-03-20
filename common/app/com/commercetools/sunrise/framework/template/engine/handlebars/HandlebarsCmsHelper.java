package com.commercetools.sunrise.framework.template.engine.handlebars;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.framework.SunriseModel;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.Optional;

final class HandlebarsCmsHelper extends SunriseModel implements Helper<String> {

    static final String CMS_PAGE_IN_CONTEXT_KEY = "context-cms-page";

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final Optional<CmsPage> cmsPage = getCmsPageFromContext(options.context);
        return cmsPage.map(page -> page.fieldOrEmpty(context)).orElse("");
    }

    private static Optional<CmsPage> getCmsPageFromContext(final Context context) {
        final Object cmsPageAsObject = context.get(CMS_PAGE_IN_CONTEXT_KEY);
        if (cmsPageAsObject instanceof CmsPage) {
            final CmsPage cmsPage = (CmsPage) cmsPageAsObject;
            return Optional.of(cmsPage);
        } else {
            return Optional.empty();
        }
    }
}
