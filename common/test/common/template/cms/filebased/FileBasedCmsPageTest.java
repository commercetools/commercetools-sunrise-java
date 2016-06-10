package common.template.cms.filebased;

import com.commercetools.sunrise.common.template.cms.CmsIdentifier;
import com.commercetools.sunrise.common.template.cms.filebased.FileBasedCmsService;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.template.i18n.yaml.YamlI18nResolver;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FileBasedCmsPageTest {

    private static final Locale DE = Locale.forLanguageTag("de");
    private static final Locale DE_AT = Locale.forLanguageTag("de-AT");
    private static final List<Locale> SUPPORTED_LOCALES = asList(DE, DE_AT);
    private static final List<String> AVAILABLE_BUNDLES = singletonList("home");

    @Test
    public void resolvesMessage() throws Exception {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of("home:header.title");
        testCms(DE, cmsIdentifier, content -> assertThat(content).contains("foo"));
    }

    @Test
    public void resolvesWithRegion() throws Exception {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of("home:header.title");
        testCms(DE_AT, cmsIdentifier, content -> assertThat(content).contains("bar"));
    }

    @Test
    public void emptyWhenContentTypeNotFound() throws Exception {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of("unknown:header.title");
        testCms(DE, cmsIdentifier, content -> assertThat(content).isEmpty());
    }

    @Test
    public void emptyWhenMessageKeyNotFound() throws Exception {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of("home:wrong.message");
        testCms(DE, cmsIdentifier, content -> assertThat(content).isEmpty());
    }

    @Test
    public void doesNotFailWithEmptyIdentifier() throws Exception {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of("");
        testCms(DE, cmsIdentifier, content -> assertThat(content).isEmpty());
    }

    private void testCms(final Locale locale, final CmsIdentifier cmsIdentifier, final Consumer<Optional<String>> test) throws Exception {
        final I18nResolver i18nResolver = YamlI18nResolver.of("cms", SUPPORTED_LOCALES, AVAILABLE_BUNDLES);
        final FileBasedCmsService cmsService = FileBasedCmsService.of(i18nResolver);
        final Optional<String> content = cmsService.get(singletonList(locale), cmsIdentifier).toCompletableFuture().join();
        test.accept(content);
    }
}
