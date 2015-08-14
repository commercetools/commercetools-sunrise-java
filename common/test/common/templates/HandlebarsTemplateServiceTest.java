package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.pages.*;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HandlebarsTemplateServiceTest {
    private static final TemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates");
    private static final TemplateLoader OVERRIDE_LOADER = new ClassPathTemplateLoader("/templates/override");
    private static final TemplateLoader WRONG_LOADER = new ClassPathTemplateLoader("/templates/wrong");

    @Test
    public void rendersTemplateWithPartial() throws Exception {
        final String html = handlebars().render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2></h2>")
                .contains("<p>default partial</p>")
                .contains("<ul></ul>");
    }

    @Test
    public void rendersOverriddenTemplateUsingOverriddenAndDefaultPartials() throws Exception {
        final String html = handlebarsWithOverride().render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("overridden template")
                .contains("overridden partial")
                .contains("another default partial");
    }

    @Test
    public void rendersDefaultTemplateUsingOverriddenAndDefaultPartials() throws Exception {
        final String html = handlebarsWithOverride().render("anotherTemplate", pageDataWithTitleAndMessage());
        assertThat(html).contains("default template")
                .contains("overridden partial")
                .contains("another default partial");
    }

    @Test
    public void throwsExceptionWhenTemplateNotFound() throws Exception {
        assertThatThrownBy(() -> handlebars().render("unknown", pageDataWithTitleAndMessage()))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    public void usesFallbackContextWhenMissingData() throws Exception {
        final String html = handlebarsWithFallbackContext(DEFAULT_LOADER).render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2>fallback unknown</h2>")
                .contains("<p>default partial</p>")
                .contains("<ul><li>fallback foo</li><li>fallback bar</li></ul>");
    }

    @Test
    public void failsSilentlyWhenFallbackContextNotFound() throws Exception {
        final String html = handlebarsWithFallbackContext(WRONG_LOADER).render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2></h2>")
                .contains("<p>default partial</p>")
                .contains("<ul></ul>");
    }

    private TemplateService handlebars() {
        return HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER));
    }

    private TemplateService handlebarsWithOverride() {
        return HandlebarsTemplateService.of(asList(OVERRIDE_LOADER, DEFAULT_LOADER));
    }

    private TemplateService handlebarsWithFallbackContext(final TemplateLoader fallbackContextLoader) {
        return HandlebarsTemplateService.of(singletonList(DEFAULT_LOADER), singletonList(fallbackContextLoader));
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
            public SeoData getSeo() {
                return null;
            }
        };
    }
}
