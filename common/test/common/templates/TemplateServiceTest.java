package common.templates;

import common.pages.PageData;
import org.junit.Test;
import play.twirl.api.Html;

import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class TemplateServiceTest {

    @Test
    public void fillsToHtml() throws Exception {
        final String expectedHtml = someHtml();
        final TemplateService templateService = (t, p, l) -> expectedHtml;
        final Html html = templateService.renderToHtml(someTemplateName(), somePageData(), singletonList(Locale.ENGLISH));
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
