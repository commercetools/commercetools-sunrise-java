package controllers;

import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class ApplicationControllerTest extends WithSunriseApplication {

    @Test
    public void indexDisplaysCategories() {
        checkCategoryCorrectlyDisplayed("en-US,en");//using existing locale
    }

    @Test
    public void englishIsFallbackLanguage() {
        checkCategoryCorrectlyDisplayed("fr-FR");//using not existing locale
    }

    private void checkCategoryCorrectlyDisplayed(String lang) {
        Result result = callAction(
                controllers.routes.ref.ApplicationController.index(),
                new FakeRequest(GET, "/").withHeader("Accept-Language", lang)
        );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentAsString(result)).contains("Snowboard equipment");
    }
}
