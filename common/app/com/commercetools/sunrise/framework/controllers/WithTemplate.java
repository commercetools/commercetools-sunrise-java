package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface WithTemplate {

    default CompletionStage<Content> renderContent(final PageContent pageContent) {
        return getTemplateRenderer().render(pageContent, getTemplateName(), getCmsPageKey());
    }

    default CompletionStage<Result> okResultWithPageContent(final PageContent pageContent) {
        return renderContent(pageContent)
                .thenApplyAsync(Results::ok, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> badRequestResultWithPageContent(final PageContent pageContent) {
        return renderContent(pageContent)
                .thenApplyAsync(Results::badRequest, HttpExecution.defaultContext());
    }

    TemplateRenderer getTemplateRenderer();

    String getTemplateName();

    @Nullable
    default String getCmsPageKey() {
        return null;
    }
}
