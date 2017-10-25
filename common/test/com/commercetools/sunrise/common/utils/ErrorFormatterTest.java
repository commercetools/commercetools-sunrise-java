package com.commercetools.sunrise.common.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.data.validation.ValidationError;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorFormatterTest {

    private static final List<Locale> LOCALES = singletonList(Locale.ENGLISH);
    private static final String MESSAGE_KEY = "error.required";
    private static final String SOME_ERROR_MESSAGE = "errorMessage";

    @Mock
    private ErrorFormatter errorFormatter;

    @Before
    public void setUp() throws Exception {
        when(errorFormatter.format(any(), any(), any())).thenReturn(SOME_ERROR_MESSAGE);
        when(errorFormatter.format(any(), any())).thenCallRealMethod();
    }

    @Test
    public void transformsToMessage() throws Exception {
        final ValidationError validationError = new ValidationError("", MESSAGE_KEY);
        test(validationError, errorFormatter ->
                verify(errorFormatter).format(LOCALES, MESSAGE_KEY, emptyMap()));
    }

    @Test
    public void transformsToMessageWithField() throws Exception {
        final ValidationError validationError = new ValidationError("username", MESSAGE_KEY);
        test(validationError, errorFormatter ->
                verify(errorFormatter).format(LOCALES, MESSAGE_KEY, singletonMap("field", "username")));
    }

    @Test
    public void transformsToMessageWithArgs() throws Exception {
        final ValidationError validationError = new ValidationError("", MESSAGE_KEY, asList("arg1", "arg2", "arg3"));
        test(validationError, errorFormatter -> {
            final Map<String, Object> hashArgs = new HashMap<>();
            hashArgs.put("0", "arg1");
            hashArgs.put("1", "arg2");
            hashArgs.put("2", "arg3");
            verify(errorFormatter).format(LOCALES, MESSAGE_KEY, hashArgs);
        });
    }

    @Test
    public void transformsToMessageWithFieldAndArgs() throws Exception {
        final ValidationError validationError = new ValidationError("username", MESSAGE_KEY, asList("arg1", "arg2", "arg3"));
        test(validationError, errorFormatter -> {
            final Map<String, Object> hashArgs = new HashMap<>();
            hashArgs.put("field", "username");
            hashArgs.put("0", "arg1");
            hashArgs.put("1", "arg2");
            hashArgs.put("2", "arg3");
            verify(errorFormatter).format(LOCALES, MESSAGE_KEY, hashArgs);
        });
    }

    private void test(final ValidationError validationError, final Consumer<ErrorFormatter> test) {
        final String errorMessage = errorFormatter.format(LOCALES, validationError);
        assertThat(errorMessage).isEqualTo(SOME_ERROR_MESSAGE);
        test.accept(errorFormatter);
    }
}
