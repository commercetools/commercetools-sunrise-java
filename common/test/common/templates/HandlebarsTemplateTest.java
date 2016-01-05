package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.controllers.*;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HandlebarsTemplateTest {
    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates");
    private static final TemplateLoader OVERRIDE_LOADER = new ClassPathTemplateLoader("/templates/override");
    private static final TemplateLoader WRONG_LOADER = new ClassPathTemplateLoader("/templates/wrong");
    private static final List<Locale> LOCALES = asList(Locale.ENGLISH, Locale.GERMAN);
    private static final List<String> BUNDLES = asList("translations", "home", "catalog", "checkout", "foo");

    @Test
    public void rendersTemplateWithPartial() throws Exception {
        final String html = handlebars().render("template", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2></h2>")
                .contains("<p>default partial</p>")
                .contains("<ul></ul>");
    }

    @Test
    public void rendersOverriddenTemplateUsingOverriddenAndDefaultPartials() throws Exception {
        final String html = handlebarsWithOverride().render("template", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("overridden template")
                .contains("overridden partial")
                .contains("another default partial");
    }

    @Test
    public void rendersDefaultTemplateUsingOverriddenAndDefaultPartials() throws Exception {
        final String html = handlebarsWithOverride().render("anotherTemplate", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("default template")
                .contains("overridden partial")
                .contains("another default partial");
    }

    @Test
    public void throwsExceptionWhenTemplateNotFound() throws Exception {
        assertThatThrownBy(() -> handlebars().render("unknown", pageDataWithTitleAndMessage(), LOCALES))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    public void usesFallbackContextWhenMissingData() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("template", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2>fallback unknown</h2>")
                .contains("<p>default partial</p>")
                .contains("<ul><li>fallback foo</li><li>fallback bar</li></ul>");
    }

    @Test
    public void failsSilentlyWhenFallbackContextNotFound() throws Exception {
        final String html = handlebarsWithFallbackContext(WRONG_LOADER).render("template", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2></h2>")
                .contains("<p>default partial</p>")
                .contains("<ul></ul>");
    }

    @Test
    public void simpleTranslation() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/simple", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("Sales Tax");
    }

    @Test
    public void simpleTranslationWithBundle() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/simpleBundle", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("Secure Checkout - Confirmation");
    }

    @Test
    public void translationWithParameter() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/parameter", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).contains("I agree to the <a id=\"confirmation-termsandconditions-link\" href=\"http://www.commercetools.com/de/\"> <span class=\"terms-link\">Terms and Conditions</span></a>");
    }

    @Test
    public void notFound() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/missing", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).isEqualTo("");
    }

    @Test
    public void notFoundInBundle() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/missingKeyInBundle", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).isEqualTo("");
    }

    @Test
    public void bundleNotFound() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/missingBundle", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).isEqualTo("");
    }

    @Test
    public void plural() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("translations/plural", pageDataWithTitleAndMessage(), LOCALES);
        assertThat(html).isEqualTo("0 items in total\n" +
                "1 item in total\n" +
                "2 items in total\n" +
                "10 items in total");
    }

    private TemplateService handlebars() {
        return HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER), LOCALES, BUNDLES, false);
    }

    private TemplateService handlebarsWithOverride() {
        return HandlebarsTemplateService.of(asList(OVERRIDE_LOADER, DEFAULT_LOADER), LOCALES, BUNDLES, false);
    }

    private TemplateService handlebarsWithFallbackContext(final TemplateLoader fallbackContextLoader) {
        return HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER), singletonList(fallbackContextLoader), LOCALES, BUNDLES, false);
    }

    private PageData pageDataWithTitleAndMessage() {
        return new PageData() {

            public String getTitle() {
                return "foo";
            }

            public String getMessage() {
                return "bar";
            }

            @Override
            public PageHeader getHeader() {
                return null;
            }

            @Override
            public PageContent getContent() {
                return null;
            }

            @Override
            public PageFooter getFooter() {
                return null;
            }

            @Override
            public PageMeta getMeta() {
                return null;
            }
        };
    }
}
