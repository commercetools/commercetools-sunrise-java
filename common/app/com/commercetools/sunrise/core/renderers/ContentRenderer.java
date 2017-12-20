package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.google.inject.ImplementedBy;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

@ImplementedBy(PageHtmlContentRenderer.class)
public interface ContentRenderer {

    CompletionStage<Content> render(final PageData pageData, @Nullable final String templateName, @Nullable final String cmsKey);

    default CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName, @Nullable final String cmsKey) {
        return render(buildPageData(pageContent), templateName, cmsKey);
    }

    default CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName) {
        return render(pageContent, templateName, null);
    }

    PageData buildPageData(final PageContent pageContent);
}
