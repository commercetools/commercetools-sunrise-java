package com.commercetools.sunrise.cms.filebased;

import com.commercetools.sunrise.cms.CmsPage;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FileBasedCmsPageTest extends WithApplication {

    private static final Locale DE = Locale.forLanguageTag("de");
    private static final Locale DE_AT = Locale.forLanguageTag("de-AT");

    private CmsMessagesApi cmsMessagesApi;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .configure("play.i18n.path", "cms")
                .configure("play.i18n.langs", asList("de", "de-AT"))
                .build();
    }

    @Before
    public void setUp() throws Exception {
        this.cmsMessagesApi = app.injector().instanceOf(CmsMessagesApi.class);
    }

    @Test
    public void resolvesMessage() throws Exception {
        testCms(DE, "home", "header.title", content -> assertThat(content).contains("foo"));
    }

    @Test
    public void resolvesWithRegion() throws Exception {
        testCms(DE_AT, "home", "header.title", content -> assertThat(content).contains("bar"));
    }

    @Test
    public void emptyWhenPageKeyNotFound() throws Exception {
        testCms(DE, "unknown", "header.title", content -> assertThat(content).isEmpty());
    }

    @Test
    public void emptyWhenFieldNameNotFound() throws Exception {
        testCms(DE, "home", "wrong.message", content -> assertThat(content).isEmpty());
    }

    @Test
    public void doesNotFailWithEmptyPageKey() throws Exception {
        testCms(DE, "", "header.title", content -> assertThat(content).isEmpty());
    }

    @Test
    public void doesNotFailWithEmptyFieldName() throws Exception {
        testCms(DE, "home", "", content -> assertThat(content).isEmpty());
    }

    private void testCms(final Locale locale, final String pageKey, final String fieldName, final Consumer<Optional<String>> test) throws Exception {
        final FileBasedCmsService cmsService = new FileBasedCmsService(cmsMessagesApi);
        final Optional<CmsPage> page = cmsService.page(pageKey, singletonList(locale)).toCompletableFuture().join();
        test.accept(page.flatMap(p -> p.field(fieldName)));
    }
}
