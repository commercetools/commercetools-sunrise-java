package com.commercetools.sunrise.core.i18n;

import io.sphere.sdk.models.LocalizedString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class I18nResolverTest {

    private static final String MESSAGE_KEY = "foo.bar";
    private static final String MESSAGE_ANSWER = "OK";
    private static final Map<String, Object> ARGS = singletonMap("arg1", "val1");

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private I18nResolver i18nResolverWithEnglish;

    @Before
    public void setUp() throws Exception {
        when(i18nResolverWithEnglish.currentLanguage()).thenReturn(ENGLISH);
        when(i18nResolverWithEnglish.get(eq(Locale.ENGLISH), eq(MESSAGE_KEY), anyMap())).thenReturn(Optional.of(MESSAGE_ANSWER));
    }

    @Test
    public void resolvesLocalizedStringWithCurrentLanguage() throws Exception {
        final LocalizedString localizedString = LocalizedString.ofEnglish("foo");
        i18nResolverWithEnglish.get(localizedString);
        verify(i18nResolverWithEnglish).get(ENGLISH, localizedString);
    }

    @Test
    public void resolvesMessageWithCurrentLanguage() throws Exception {
        assertThat(i18nResolverWithEnglish.get(MESSAGE_KEY, ARGS)).isEqualTo(Optional.of(MESSAGE_ANSWER));
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, ARGS);
    }

    @Test
    public void resolvesMessageWithCurrentLanguageAndEmptyArgs() throws Exception {
        final Optional<String> actual = i18nResolverWithEnglish.get(MESSAGE_KEY);
        assertThat(actual).isEqualTo(Optional.of(MESSAGE_ANSWER));
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, emptyMap());
    }

    @Test
    public void resolvesMessageOrEmptyWithCurrentLanguage() throws Exception {
        assertThat(i18nResolverWithEnglish.getOrEmpty(MESSAGE_KEY, ARGS)).isEqualTo(MESSAGE_ANSWER);
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, ARGS);

        assertThat(i18nResolverWithEnglish.getOrEmpty("unknown", ARGS)).isEmpty();
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, ARGS);
    }

    @Test
    public void resolvesMessageOrEmptyWithCurrentLanguageAndEmptyArgs() throws Exception {
        assertThat(i18nResolverWithEnglish.getOrEmpty(MESSAGE_KEY)).isEqualTo(MESSAGE_ANSWER);
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, emptyMap());

        assertThat(i18nResolverWithEnglish.getOrEmpty("unknown")).isEmpty();
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, emptyMap());
    }

    @Test
    public void resolvesMessageOrKeyWithCurrentLanguage() throws Exception {
        assertThat(i18nResolverWithEnglish.getOrKey(MESSAGE_KEY, ARGS)).isEqualTo(MESSAGE_ANSWER);
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, ARGS);

        assertThat(i18nResolverWithEnglish.getOrKey("unknown", ARGS)).isEqualTo("unknown");
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, ARGS);
    }

    @Test
    public void resolvesMessageOrKeyWithCurrentLanguageAndEmptyArgs() throws Exception {
        assertThat(i18nResolverWithEnglish.getOrKey(MESSAGE_KEY)).isEqualTo(MESSAGE_ANSWER);
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, emptyMap());

        assertThat(i18nResolverWithEnglish.getOrKey("unknown")).isEqualTo("unknown");
        verify(i18nResolverWithEnglish).get(ENGLISH, MESSAGE_KEY, emptyMap());
    }
}
