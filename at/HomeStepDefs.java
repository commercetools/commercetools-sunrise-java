import cucumber.api.java8.En;
import org.fluentlenium.core.Fluent;

import static org.assertj.core.api.Assertions.assertThat;


public class HomeStepDefs implements En {

    private final Fluent browser = RunCukesTest.fluentAdapter();

    public HomeStepDefs() {
        Given("I navigate to \"/(.*)\"", (final String path) -> {
            browser.goTo(path);
        });

        Then("I see the text \"(.*)\"", (final String text) -> {
            assertThat(browser.pageSource()).contains(text);
        });
    }
}