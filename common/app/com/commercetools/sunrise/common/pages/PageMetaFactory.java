package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import play.filters.csrf.CSRF;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class PageMetaFactory extends ViewModelFactory<PageMeta, PageContent> {

    private final Http.Context httpContext;

    @Inject
    public PageMetaFactory(final Http.Context httpContext) {
        this.httpContext = httpContext;
    }

    @Override
    protected PageMeta getViewModelInstance() {
        return new PageMeta();
    }

    public final PageMeta create(final PageContent content) {
        return initializedViewModel(content);
    }

    @Override
    protected final void initialize(final PageMeta viewModel, final PageContent content) {
        fillBagQuantityOptions(viewModel, content);
        fillCsrfToken(viewModel, content);
        fillSelfPageUrl(viewModel, content);
    }

    protected void fillCsrfToken(final PageMeta model, final PageContent content) {
        CSRF.getToken(httpContext.request())
                .map(CSRF.Token::value)
                .ifPresent(model::setCsrfToken);
    }

    protected void fillBagQuantityOptions(final PageMeta model, final PageContent content) {
        model.setBagQuantityOptions(IntStream
                .rangeClosed(1, 9)
                .boxed()
                .collect(toList()));
    }

    protected void fillSelfPageUrl(final PageMeta model, final PageContent content) {
        model.addHalLinkOfHrefAndRel(httpContext.request().uri(), "self");
    }
}
