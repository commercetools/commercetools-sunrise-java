package common.cms;

import org.junit.Test;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.i18n.MessagesApi;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Locale;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayCmsPageTest extends WithApplication {
    private static final Locale DE = Locale.forLanguageTag("de");
    private static final Locale DE_AT = Locale.forLanguageTag("de-AT");

    @Test
    public void getsMessage() throws Exception {
        assertThat(cms(DE, "home").get("title")).contains("foo");
    }

    @Test
    public void getsMessageWithoutPageKey() throws Exception {
        assertThat(cms(DE).get("home.title")).contains("foo");
    }

    @Test
    public void getsMessageWithRegion() throws Exception {
        assertThat(cms(DE_AT, "home").get("title")).contains("bar");
    }

    @Test
    public void getsEmptyWhenKeyNotFound() throws Exception {
        assertThat(cms(DE).get("wrong.message")).isEmpty();
    }

    @Override
    protected Application provideApplication() {
        final Configuration config = new Configuration(singletonMap("play.i18n.langs", asList(DE.toLanguageTag(), DE_AT.toLanguageTag())));
        final Configuration fallbackConfig = Configuration.load(new Environment(Mode.TEST));
        return new GuiceApplicationBuilder().loadConfig(config.withFallback(fallbackConfig)).build();

    }

    private CmsPage cms(final Locale locale) {
        return cms(locale, null);
    }

    private CmsPage cms(final Locale locale, final String pageKey) {
        final MessagesApi messagesApi = app.injector().instanceOf(MessagesApi.class);
        try {
            return PlayCmsService.of(messagesApi).getPage(locale, pageKey).toCompletableFuture().get(1, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new CompletionException(e);
        }
    }
}
