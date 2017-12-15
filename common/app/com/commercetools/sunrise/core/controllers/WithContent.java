package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface WithContent {

    default CompletionStage<Content> renderContent(final PageContent pageContent) {
        return getContentRenderer().render(pageContent, getTemplateName(), getCmsPageKey());
    }

    default CompletionStage<Result> okResultWithPageContent(final PageContent pageContent) {
        return renderContent(pageContent)
                .thenApplyAsync(Results::ok, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> badRequestResultWithPageContent(final PageContent pageContent) {
        return renderContent(pageContent)
                .thenApplyAsync(Results::badRequest, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> internalServerErrorResultWithPageContent(final PageContent pageContent) {
        return renderContent(pageContent)
                .thenApplyAsync(Results::internalServerError, HttpExecution.defaultContext());
    }

    ContentRenderer getContentRenderer();

    @Nullable
    default String getTemplateName() {
        return null;
    }

    @Nullable
    default String getCmsPageKey() {
        return null;
    }
}
