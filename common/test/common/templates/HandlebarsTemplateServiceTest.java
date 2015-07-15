package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import common.pages.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HandlebarsTemplateServiceTest {
    private static final ClassPathTemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates");
    private static final ClassPathTemplateLoader OVERRIDE_LOADER = new ClassPathTemplateLoader("/templates/override");

    @Test
    public void rendersTemplateWithPartial() throws Exception {
        final String html = handlebars().render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<h2></h2>")
                .contains("<p>default partial</p>");
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

    private HandlebarsTemplateService handlebarsWithOverride() {
        return HandlebarsTemplateService.of(OVERRIDE_LOADER, DEFAULT_LOADER);
    }

    private HandlebarsTemplateService handlebars() {
        return HandlebarsTemplateService.of(DEFAULT_LOADER);
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
