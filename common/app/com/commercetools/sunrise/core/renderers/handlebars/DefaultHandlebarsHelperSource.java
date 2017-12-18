package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.models.products.ProductPriceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.money.MonetaryAmount;
import java.util.Optional;

import static com.commercetools.sunrise.core.renderers.handlebars.HandlebarsTemplateEngine.CMS_PAGE_IN_CONTEXT_KEY;

@Singleton
public class DefaultHandlebarsHelperSource implements HandlebarsHelperSource {

    private final I18nResolver i18nResolver;
    private final ProductReverseRouter productReverseRouter;
    private final PriceFormatter priceFormatter;

    @Inject
    protected DefaultHandlebarsHelperSource(final I18nResolver i18nResolver, ProductReverseRouter productReverseRouter, PriceFormatter priceFormatter) {
        this.i18nResolver = i18nResolver;
        this.productReverseRouter = productReverseRouter;
        this.priceFormatter = priceFormatter;
    }

    /**
     * Registrable {@code i18n} helper to obtain the correct translation for the given {@code messageKey}.
     * @param messageKey the message key to translate
     * @param options available options
     * @return the translated message
     */
    public CharSequence i18n(final String messageKey, final Options options) {
        return i18nResolver.getOrEmpty(messageKey, options.hash);
    }

    /**
     * Registrable {@code cms} helper to obtain the CMS content for the given {@code fieldName}.
     * @param fieldName the field name corresponding to the content
     * @param options available options
     * @return the content
     */
    public CharSequence cms(final String fieldName, final Options options) {
        final Optional<CmsPage> cmsPage = getCmsPageFromContext(options.context);
        return cmsPage.flatMap(page -> page.field(fieldName)).orElse("");
    }

    /**
     * Registrable {@code json} helper to obtain the JSON string representation of a Java object.
     * @param object the Java object
     * @return the JSON string representation
     * @throws JsonProcessingException if the given object could not be processed into JSON
     */
    public CharSequence json(final Object object) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private static Optional<CmsPage> getCmsPageFromContext(final Context context) {
        final Object cmsPageAsObject = context.get(CMS_PAGE_IN_CONTEXT_KEY);
        if (cmsPageAsObject instanceof CmsPage) {
            return Optional.of((CmsPage) cmsPageAsObject);
        } else {
            return Optional.empty();
        }
    }

    @Nullable
    public CharSequence imageUrl(final ProductVariant object) {
        return object.getImages().stream().map(image -> (CharSequence) image.getUrl()).findFirst().orElse(null);
    }

    public CharSequence variantUrlFromLineItem(final LineItem object) {
        return productReverseRouter.productDetailPageCall(object).map(Call::url).orElse("");
    }

    public CharSequence currentPriceForLineItem(final LineItem object) {
        return formatPrice(ProductPriceUtils.calculateAppliedProductPrice(object));
    }

    public CharSequence oldPriceForLineItem(final LineItem object) {
        return ProductPriceUtils.calculatePreviousProductPrice(object)
                .map(oldPrice -> formatPrice(oldPrice)).orElse("");
    }

    public CharSequence formatPrice(final MonetaryAmount object) {
        return priceFormatter.format(object);
    }
}
