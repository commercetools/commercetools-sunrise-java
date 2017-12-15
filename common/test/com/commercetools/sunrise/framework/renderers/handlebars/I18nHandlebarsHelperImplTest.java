package com.commercetools.sunrise.framework.renderers.handlebars;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.github.jknack.handlebars.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class I18nHandlebarsHelperImplTest {

    private static final String MESSAGE_KEY = "foo.bar";
    private static final String MESSAGE_KEY_ANSWER = "OK";
    private static final Map<String, Object> ARGS = singletonMap("name", "John");

    @Mock
    private I18nResolver fakeI18nResolver;

    @InjectMocks
    private DefaultHandlebarsHelperSource helperSource;

    @Before
    public void setUp() throws Exception {
        when(fakeI18nResolver.get(eq(MESSAGE_KEY), any())).thenReturn(Optional.of(MESSAGE_KEY_ANSWER));
        when(fakeI18nResolver.getOrEmpty(any(), any())).thenCallRealMethod();
    }

    @Test
    public void translatesMessage() throws Exception {
        assertThat(helperSource.i18n(MESSAGE_KEY, optionsWithoutArgs())).isEqualTo(MESSAGE_KEY_ANSWER);
        verify(fakeI18nResolver).getOrEmpty(MESSAGE_KEY, emptyMap());
    }

    @Test
    public void returnsEmptyOnUndefinedMessage() throws Exception {
        assertThat(helperSource.i18n("unknown", optionsWithoutArgs())).isEmpty();
        verify(fakeI18nResolver).getOrEmpty("unknown", emptyMap());
    }

    @Test
    public void translatesMessageWithArgs() throws Exception {
        assertThat(helperSource.i18n(MESSAGE_KEY, optionsWithArgs())).isEqualTo(MESSAGE_KEY_ANSWER);
        verify(fakeI18nResolver).getOrEmpty(MESSAGE_KEY, ARGS);
    }

    private Options optionsWithoutArgs() {
        return optionsBuilder()
                .build();
    }

    private Options optionsWithArgs() {
        return optionsBuilder()
                .setHash(ARGS)
                .build();
    }

    private static Options.Builder optionsBuilder() {
        return new Options.Builder(null, null, null, null, null);
    }
}
