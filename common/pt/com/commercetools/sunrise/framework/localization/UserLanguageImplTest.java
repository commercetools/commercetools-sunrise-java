package com.commercetools.sunrise.framework.localization;

import org.junit.Test;
import play.test.WithApplication;

import java.util.Locale;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.HeaderNames.ACCEPT_LANGUAGE;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class UserLanguageImplTest extends WithApplication {

    @Test
    public void addsAcceptedLanguages() throws Exception {
        final ProjectContext projectContext = mock(ProjectContext.class);
        when(projectContext.isLocaleSupported(any())).thenReturn(true);
        invokeWithContext(fakeRequest().header(ACCEPT_LANGUAGE, "de, en-US"), () ->
                assertThat(userLanguageWithEnglishSelected(projectContext).locales())
                        .extracting(Locale::toLanguageTag)
                        .containsExactly("en", "de", "en-US"));
    }

    @Test
    public void discardsDuplicated() throws Exception {
        final ProjectContext projectContext = mock(ProjectContext.class);
        when(projectContext.isLocaleSupported(any())).thenReturn(true);
        invokeWithContext(fakeRequest().header(ACCEPT_LANGUAGE, "de, en-US, en"), () ->
                assertThat(userLanguageWithEnglishSelected(projectContext).locales())
                        .extracting(Locale::toLanguageTag)
                        .containsExactly("en", "de", "en-US"));
    }

    @Test
    public void discardsNotSupportedByProject() throws Exception {
        final ProjectContext projectContext = mock(ProjectContext.class);
        when(projectContext.isLocaleSupported(any())).thenReturn(false);
        invokeWithContext(fakeRequest().header(ACCEPT_LANGUAGE, "de, en-US"), () ->
                assertThat(userLanguageWithEnglishSelected(projectContext).locales())
                        .extracting(Locale::toLanguageTag)
                        .containsExactly("en"));
    }

    @Test
    public void doesNotFailOnHttpContextNotAvailable() throws Exception {
        final ProjectContext projectContext = mock(ProjectContext.class);
        assertThat(userLanguageWithEnglishSelected(projectContext).locales())
                .extracting(Locale::toLanguageTag)
                .containsExactly("en");
    }

    private UserLanguage userLanguageWithEnglishSelected(final ProjectContext projectContext) {
        return new UserLanguageImpl(Locale.forLanguageTag("en"), projectContext);
    }
}