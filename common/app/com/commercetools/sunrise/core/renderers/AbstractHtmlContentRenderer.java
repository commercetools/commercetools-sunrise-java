package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.core.viewmodels.OldPageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.twirl.api.Content;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractHtmlContentRenderer implements ContentRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentRenderer.class);

    private final Locale locale;
    private final TemplateEngine templateEngine;
    private final CmsService cmsService;

    protected AbstractHtmlContentRenderer(final Locale locale, final TemplateEngine templateEngine, final CmsService cmsService) {
        this.locale = locale;
        this.templateEngine = templateEngine;
        this.cmsService = cmsService;
    }

    @Override
    public CompletionStage<Content> render(final OldPageData oldPageData, @Nullable final String templateName, @Nullable final String cmsKey) {
        if (cmsKey != null) {
            return cmsService.page(cmsKey, singletonList(locale))
                    .thenApplyAsync(cmsPage -> renderHtml(oldPageData, templateName, cmsPage.orElse(null)), HttpExecution.defaultContext());
        } else {
            return completedFuture(renderHtml(oldPageData, templateName, null));
        }
    }

    private Html renderHtml(final OldPageData oldPageData, @Nullable final String templateName, @Nullable final CmsPage cmsPage) {
        final String content;
        if (templateName != null) {
            final TemplateContext templateContext = new TemplateContext(oldPageData, cmsPage);
            content = templateEngine.render(templateName, templateContext);
        } else {
            LOGGER.warn("HTML renderer used without template, probably this is not what you intended");
            content = oldPageData.toString();
        }
        return new Html(content);
    }
}
