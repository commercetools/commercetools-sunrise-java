package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.core.hooks.HookContext;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
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

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public abstract class AbstractTemplateEngine implements TemplateEngine {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateEngine.class);
    private static final ObjectWriter OBJECT_WRITER = createObjectWriter();

    private final HookContext hookContext;

    protected AbstractTemplateEngine(final HookContext hookContext) {
        this.hookContext = hookContext;
    }

    protected final CompletionStage<PageData> applyPageDataHooks(final PageData pageData) {
        final CompletionStage<PageData> pageDataStage = PageDataHook.runHook(hookContext, pageData);
        pageDataStage.thenAccept(AbstractTemplateEngine::logPageData);
        return pageDataStage;
    }

    private static void logPageData(final PageData pageData) {
        if (LOGGER.isTraceEnabled()) {
            try {
                LOGGER.trace(OBJECT_WRITER.writeValueAsString(pageData));
            } catch (final Exception e) {
                LOGGER.error("Serialization of PageData failed.", e);
            }
        }
    }

    private static ObjectWriter createObjectWriter() {
        return new ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .registerModule(formModule())
                .writer()
                .withDefaultPrettyPrinter();
    }

    private static SimpleModule formModule() {
        return new SimpleModule("form module")
                .addSerializer(Form.class, new StdScalarSerializer<Form>(Form.class) {
                    @Override
                    public void serialize(final Form form, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
                        gen.writeObject(form.data());
                    }
                });
    }
}
