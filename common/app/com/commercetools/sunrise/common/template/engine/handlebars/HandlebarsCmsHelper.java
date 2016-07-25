package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.template.cms.CmsPage;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.util.Optional;

final class HandlebarsCmsHelper extends Base implements Helper<String> {

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final Optional<CmsPage> cmsPage = HelperUtils.getCmsPageFromContext(options.context);
        return cmsPage.map(page -> page.getOrEmpty(context)).orElse("");
    }
}
