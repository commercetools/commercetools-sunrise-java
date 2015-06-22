package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import common.pages.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsTemplateServiceTest {
    private static final ClassPathTemplateLoader DEFAULT_LOADER = new ClassPathTemplateLoader("/templates");
    private static final ClassPathTemplateLoader OVERRIDE_LOADER = new ClassPathTemplateLoader("/templates/override");

    @Test
    public void fillsTemplate() throws Exception {
        final String html = handlebars().render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<p></p>");
    }

    @Test
    public void fillsOverriddenTemplate() throws Exception {
        final String html = handlebarsWithOverride().render("template", pageDataWithTitleAndMessage());
        assertThat(html).contains("<title>more foo</title>")
                .contains("<h1>more bar</h1>")
                .contains("<p>more </p>");
    }

    private HandlebarsTemplateService handlebarsWithOverride() {
        return HandlebarsTemplateService.of(DEFAULT_LOADER, OVERRIDE_LOADER);
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
