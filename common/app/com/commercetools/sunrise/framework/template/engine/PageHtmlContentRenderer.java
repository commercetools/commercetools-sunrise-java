package com.commercetools.sunrise.framework.template.engine;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.framework.hooks.RequestHookRunner;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.localization.UserLanguage;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.viewmodels.PageDataFactory;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

final class PageHtmlContentRenderer extends AbstractHtmlContentRenderer implements ContentRenderer {

    private static final Logger PAGE_DATA_LOGGER_AS_JSON = LoggerFactory.getLogger(PageData.class.getName() + "Json");
    private static final ObjectMapper objectMapper = createObjectMapper();

    private final PageDataFactory pageDataFactory;
    private final RequestHookRunner hookRunner;

    @Inject
    PageHtmlContentRenderer(final UserLanguage userLanguage, final TemplateEngine templateEngine, final CmsService cmsService,
                            final PageDataFactory pageDataFactory, final RequestHookRunner hookRunner) {
        super(userLanguage, templateEngine, cmsService);
        this.pageDataFactory = pageDataFactory;
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName, @Nullable final String cmsKey) {
        final PageData pageData = pageDataFactory.create(pageContent);
        return hookRunner.waitForHookedComponentsToFinish()
                .thenComposeAsync(unused -> {
                    PageDataReadyHook.runHook(hookRunner, pageData);
                    logFinalPageData(pageData);
                    return super.render(pageData, templateName, cmsKey);
                }, HttpExecution.defaultContext());
    }

    private static void logFinalPageData(final PageData pageData) {
        if (PAGE_DATA_LOGGER_AS_JSON.isDebugEnabled()) {
            try {
                final ObjectWriter objectWriter = objectMapper.writer()
                        .withDefaultPrettyPrinter();
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
