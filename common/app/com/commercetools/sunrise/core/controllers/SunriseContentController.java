package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.ContentRenderer;

public abstract class SunriseContentController extends SunriseController {

    private final ContentRenderer contentRenderer;

    protected SunriseContentController(final ContentRenderer contentRenderer) {
        this.contentRenderer = contentRenderer;
    }

    public ContentRenderer getContentRenderer() {
        return contentRenderer;
    }
}
