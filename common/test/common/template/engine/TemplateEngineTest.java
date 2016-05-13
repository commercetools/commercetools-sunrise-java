package common.template.engine;

import common.controllers.PageData;
import org.junit.Test;
import play.twirl.api.Html;

import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class TemplateEngineTest {

    @Test
    public void fillsToHtml() throws Exception {
        final String expectedHtml = someHtml();
        final TemplateEngine templateEngine = (t, p, l) -> expectedHtml;
        final Html html = templateEngine.renderToHtml(someTemplateName(), somePageData(), singletonList(Locale.ENGLISH));
        assertThat(html.body()).isEqualTo(expectedHtml);
    }

    private String someHtml() {
        return "<html>" +
                "<head></head>" +
                "<body>Hello world!</body" +
                "</html>";
    }

    private String someTemplateName() {
        return "foo";
    }

    private PageData somePageData() {
        return null;
    }
}
