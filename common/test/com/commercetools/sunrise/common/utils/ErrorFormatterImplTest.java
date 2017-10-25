package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorFormatterImplTest {

    private static final List<Locale> LOCALES = singletonList(Locale.ENGLISH);
    private static final String MESSAGE_KEY = "error.required";
    private static final I18nIdentifier I18N_IDENTIFIER = I18nIdentifier.of("main", MESSAGE_KEY);
    private static final Map<String, Object> ARGS_WITH_FIELD = singletonMap("field", "username");

    @Mock
    private I18nResolver i18nResolver;
    @Spy
    private I18nIdentifierFactory i18nIdentifierFactory;

    @InjectMocks
    private ErrorFormatterImpl errorFormatter;

    @Before
    public void setUp() throws Exception {
        when(i18nResolver.getOrKey(any(), any(), any())).thenCallRealMethod();
    }

    @Test
    public void translatesMessageKey() throws Exception {
        mockI18nResolverWithError();
        final String errorMessage = errorFormatter.format(LOCALES, MESSAGE_KEY, emptyMap());
        assertThat(errorMessage).isEqualTo("Required field");
    }

    @Test
    public void returnsMessageKeyWhenNoMatch() throws Exception {
        mockI18nResolverWithoutError();
        final String errorMessage = errorFormatter.format(LOCALES, MESSAGE_KEY, emptyMap());
        assertThat(errorMessage).isEqualTo(MESSAGE_KEY);
    }

    @Test
    public void returnsMessageKeyWithFieldIfProvided() throws Exception {
        mockI18nResolverWithError();
        final String errorMessage = errorFormatter.format(LOCALES, MESSAGE_KEY, ARGS_WITH_FIELD);
        assertThat(errorMessage).isEqualTo("Required field: username");
    }

    private void mockI18nResolverWithoutError() {
        when(i18nResolver.get(any(), eq(I18N_IDENTIFIER), anyMap())).thenReturn(Optional.empty());
    }

    private void mockI18nResolverWithError() {
        when(i18nResolver.get(any(), eq(I18N_IDENTIFIER), anyMap())).thenReturn(Optional.of("Required field"));
        when(i18nResolver.get(any(), eq(I18N_IDENTIFIER), eq(singletonMap("field", "username")))).thenReturn(Optional.of("Required field: username"));
    }
}
