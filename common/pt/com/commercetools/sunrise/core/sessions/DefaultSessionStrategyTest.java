package com.commercetools.sunrise.core.sessions;

import org.junit.Test;
import play.mvc.Http;
import play.test.WithApplication;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class DefaultSessionStrategyTest extends WithApplication {

    private static final String SOME_KEY = "some-key";
    private static final String SOME_VALUE = "some-value";

    @Test
    public void findsWhenInSession() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, SOME_VALUE)), () ->
                assertThat(strategy().findValueByKey(SOME_KEY)).contains(SOME_VALUE));
    }

    @Test
    public void doesNotFindWhenNotInSession() throws Exception {
        invokeWithContext(fakeRequest(), () ->
                assertThat(strategy().findValueByKey(SOME_KEY)).isEmpty());
    }

    @Test
    public void createsWhenNotFound() throws Exception {
        invokeWithContext(fakeRequest(), () -> {
            final DefaultSessionStrategy strategy = strategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).isEmpty();
            strategy.overwriteValueByKey(SOME_KEY, SOME_VALUE);
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            return strategy;
        });
    }

    @Test
    public void replacesValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, SOME_VALUE)), () -> {
            final DefaultSessionStrategy strategy = strategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            strategy.overwriteValueByKey(SOME_KEY, "some-other-value");
            assertThat(strategy.findValueByKey(SOME_KEY)).contains("some-other-value");
            return strategy;
        });
    }

    @Test
    public void removesKeyOnNullValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, SOME_VALUE)), () -> {
            final DefaultSessionStrategy strategy = strategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            strategy.overwriteValueByKey(SOME_KEY, null);
            assertThat(strategy.findValueByKey(SOME_KEY)).isEmpty();
            return strategy;
        });
    }

    @Test
    public void removesValue() throws Exception {
        invokeWithContext(fakeRequest().session(singletonMap(SOME_KEY, SOME_VALUE)), () -> {
            final DefaultSessionStrategy strategy = strategy();
            assertThat(strategy.findValueByKey(SOME_KEY)).contains(SOME_VALUE);
            strategy.removeValueByKey(SOME_KEY);
            assertThat(strategy.findValueByKey(SOME_KEY)).isEmpty();
            assertThat(Http.Context.current().session().containsKey(SOME_KEY)).isFalse();
            return strategy;
        });
    }

    private DefaultSessionStrategy strategy() {
        return new DefaultSessionStrategy();
    }
}
