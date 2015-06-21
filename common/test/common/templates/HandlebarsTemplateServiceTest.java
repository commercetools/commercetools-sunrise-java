package common.templates;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import common.pages.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarsTemplateServiceTest {

    @Test
    public void fillsTemplate() throws Exception {
        final String html = handlebars().fill("template", somePageData());
        assertThat(html).contains("<title>foo</title>")
                .contains("<h1>bar</h1>")
                .contains("<p></p>");
    }

    @Test
    public void fillsOverriddenTemplate() throws Exception {
        final String html = handlebarsWithOverride().fill("template", somePageData());
        assertThat(html).contains("<title>more foo</title>")
                .contains("<h1>more bar</h1>")
                .contains("<p>more </p>");
    }

    private HandlebarsTemplateService handlebarsWithOverride() {
        return HandlebarsTemplateService.of(new ClassPathTemplateLoader(), new ClassPathTemplateLoader("/override"));
    }

    private HandlebarsTemplateService handlebars() {
        return HandlebarsTemplateService.of(new ClassPathTemplateLoader());
    }

    private PageData somePageData() {
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
