package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;

import javax.inject.Inject;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class EmailHtmlContentRenderer extends AbstractHtmlContentRenderer implements EmailContentRenderer {

    @Inject
    EmailHtmlContentRenderer(final Locale locale, final TemplateEngine templateEngine, final CmsService cmsService) {
        super(locale, templateEngine, cmsService);
    }

    @Override
    public CompletionStage<PageData> buildPageData(final PageContent pageContent) {
        final PageData pageData = new PageData();
        pageData.setContent(pageContent);
        return completedFuture(pageData);
    }
}
