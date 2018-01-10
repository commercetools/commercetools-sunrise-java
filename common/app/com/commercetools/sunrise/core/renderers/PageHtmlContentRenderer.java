package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.core.hooks.RequestHookRunner;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.viewmodels.PageDataFactory;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.carts.MyCartInCache;
import com.commercetools.sunrise.models.categories.NavigationCategoryTree;
import com.commercetools.sunrise.models.customers.MyCustomerInCache;
import com.commercetools.sunrise.models.shoppinglists.MyWishlistFetcher;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import io.sphere.sdk.categories.CategoryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

final class PageHtmlContentRenderer extends AbstractHtmlContentRenderer implements ContentRenderer {

    private static final Logger PAGE_DATA_LOGGER_AS_JSON = LoggerFactory.getLogger(PageData.class.getName() + "Json");
    private static final ObjectMapper objectMapper = createObjectMapper();

    private final PageDataFactory pageDataFactory;
    private final RequestHookRunner hookRunner;
    private final CategoryTree categoryTree;
    private final MyCartInCache myCartInCache;
    private final MyCustomerInCache myCustomerInCache;
    private final MyWishlistFetcher wishlistFinder;

    @Inject
    PageHtmlContentRenderer(final Locale locale, final TemplateEngine templateEngine, final CmsService cmsService,
                            final PageDataFactory pageDataFactory, final RequestHookRunner hookRunner,
                            @NavigationCategoryTree final CategoryTree categoryTree, final MyCartInCache myCartInCache,
                            final MyCustomerInCache myCustomerInCache, final MyWishlistFetcher wishlistFinder) {
        super(locale, templateEngine, cmsService);
        this.pageDataFactory = pageDataFactory;
        this.hookRunner = hookRunner;
        this.categoryTree = categoryTree;
        this.myCartInCache = myCartInCache;
        this.myCustomerInCache = myCustomerInCache;
        this.wishlistFinder = wishlistFinder;
    }

    @Override
    public CompletionStage<Content> render(final PageData pageData, @Nullable final String templateName, @Nullable final String cmsKey) {
        return hookRunner.waitForHookedComponentsToFinish()
                .thenComposeAsync(unused -> {
                    PageDataReadyHook.runHook(hookRunner, pageData);
                    logFinalPageData(pageData);
                    return super.render(pageData, templateName, cmsKey);
                }, HttpExecution.defaultContext());
    }

    @Override
    public CompletionStage<PageData> buildPageData(final PageContent pageContent) {
        final PageData pageData = pageDataFactory.create(pageContent);
        pageData.put("product", pageContent.get("myproduct"));
        pageData.put("variant", pageContent.get("myvariant"));
        pageData.put("categoryTree", categoryTree);
        return myCartInCache.get().thenComposeAsync(cart ->
                myCustomerInCache.get().thenComposeAsync(customer ->
                        wishlistFinder.get().thenApply(wishlist -> {
                            pageData.put("cart", cart.orElse(null));
                            pageData.put("customer", customer.orElse(null));
                            pageData.getContent().put("wishlist", wishlist.orElse(null));
                            return pageData;
                        }), HttpExecution.defaultContext()), HttpExecution.defaultContext());
    }

    private static void logFinalPageData(final PageData pageData) {
        if (PAGE_DATA_LOGGER_AS_JSON.isDebugEnabled()) {
            try {
                final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
                final String formatted = objectWriter.writeValueAsString(pageData);
                PAGE_DATA_LOGGER_AS_JSON.debug(formatted);
            } catch (final Exception e) {
                PAGE_DATA_LOGGER_AS_JSON.error("serialization of " + pageData + " failed.", e);
            }
        }
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        final SimpleModule mod = new SimpleModule("form module");
        mod.addSerializer(Form.class, new StdScalarSerializer<Form>(Form.class){
            @Override
            public void serialize(final Form value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
                gen.writeObject(value.data());
            }
        });
        mapper.registerModule(mod);
        return mapper;
    }
}
