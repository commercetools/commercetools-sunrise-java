package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CompositeI18nResolverTest {

    @Test
    public void resolvesWithFirstResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(
                (bundle, key, locale) -> Optional.of("foo"),
                (bundle, key, locale) -> Optional.of("bar"));
        testCompositeResolver(i18nResolverList, message -> assertThat(message).contains("foo"));
    }

    @Test
    public void fallbacksToSecondResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(
                (bundle, key, locale) -> Optional.empty(),
                (bundle, key, locale) -> Optional.of("bar"));
        testCompositeResolver(i18nResolverList, message -> assertThat(message).contains("bar"));
    }

    @Test
    public void emptyWhenNotFoundInAnyResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(
                (bundle, key, locale) -> Optional.empty(),
                (bundle, key, locale) -> Optional.empty());
        testCompositeResolver(i18nResolverList, message -> assertThat(message).isEmpty());
    }

    public void testCompositeResolver(final List<I18nResolver> i18nResolverList, final Consumer<Optional<String>> test) {
        final CompositeI18nResolver i18nResolver = CompositeI18nResolver.of(i18nResolverList);
        final Optional<String> message = i18nResolver.get("bundle", "key", Locale.ENGLISH);
        test.accept(message);
    }

}
