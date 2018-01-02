package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.google.inject.ImplementedBy;
import play.libs.concurrent.HttpExecution;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

@ImplementedBy(PageHtmlContentRenderer.class)
public interface ContentRenderer {

    CompletionStage<Content> render(final PageData pageData, @Nullable final String templateName, @Nullable final String cmsKey);

    default CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName, @Nullable final String cmsKey) {
        return buildPageData(pageContent).thenComposeAsync(page ->
                render(page, templateName, cmsKey), HttpExecution.defaultContext());
    }

    default CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName) {
        return render(pageContent, templateName, null);
    }

    CompletionStage<PageData> buildPageData(final PageContent pageContent);
}
