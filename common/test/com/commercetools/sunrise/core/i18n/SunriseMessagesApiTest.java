package com.commercetools.sunrise.core.i18n;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.api.i18n.Lang;
import scala.Tuple2;
import scala.collection.mutable.Buffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static scala.collection.JavaConverters.asScalaBufferConverter;

@RunWith(MockitoJUnitRunner.class)
public class SunriseMessagesApiTest {

    private static final Lang LANG = Lang.apply("en");
    private static final String MESSAGE_KEY = "foo";
    private static final String MESSAGE_ANSWER = "OK";

    @Mock
    private com.commercetools.sunrise.core.i18n.api.SunriseMessagesApi fakeSunriseMessagesApi;

    @InjectMocks
    private SunriseMessagesApi api;

    @Before
    public void setUp() throws Exception {
        when(fakeSunriseMessagesApi.apply(eq(MESSAGE_KEY), any(), eq(LANG))).thenReturn(MESSAGE_ANSWER);
    }

    @Test
    public void convertsMapToListOfScalaTuples() throws Exception {
        final Map<String, String> args = new HashMap<>();
        args.put("firstName", "John");
        args.put("lastName", "Doe");
        assertThat(api.get(LANG, MESSAGE_KEY, args)).isEqualTo(MESSAGE_ANSWER);

        final Buffer<Object> expectedArgs = toExpectedArgsFormat(
                new Tuple2<>("firstName", "John"),
                new Tuple2<>("lastName", "Doe"));
        verify(fakeSunriseMessagesApi).apply(MESSAGE_KEY, expectedArgs, LANG);
    }

    @Test
    public void keepsRegularBehaviourWithArgs() throws Exception {
        assertThat(api.get(LANG, MESSAGE_KEY, "John", "Doe")).isEqualTo(MESSAGE_ANSWER);
        verify(fakeSunriseMessagesApi).apply(MESSAGE_KEY, toExpectedArgsFormat("John", "Doe"), LANG);
    }

    @Test
    public void keepsRegularBehaviourWithLists() throws Exception {
        final List<String> args = asList("John", "Doe");
        assertThat(api.get(LANG, MESSAGE_KEY, args)).isEqualTo(MESSAGE_ANSWER);
        verify(fakeSunriseMessagesApi).apply(MESSAGE_KEY, toExpectedArgsFormat(args), LANG);
    }

    private static Buffer<Object> toExpectedArgsFormat(final Object... args) {
        return asScalaBufferConverter(asList(args)).asScala();
    }

}
