package controllers.shoppingcart;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.content.SunriseCartContentController;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class CartContentController extends SunriseCartContentController {

    @Inject
    public CartContentController(final ContentRenderer contentRenderer,
                                 final CartPageContentFactory pageContentFactory) {
        super(contentRenderer, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
