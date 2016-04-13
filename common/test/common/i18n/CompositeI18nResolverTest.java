package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class CompositeI18nResolverTest {

    public static final I18nResolver I18N_RETURNS_ALWAYS_EMPTY = (locale, i18nIdentifier, hashArgs) -> Optional.empty();

    @Test
    public void resolvesWithFirstResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(i18nReturnsAlwaysValue("foo"), i18nReturnsAlwaysValue("bar"));
        testCompositeResolver(i18nResolverList, message -> assertThat(message).contains("foo"));
    }

    @Test
    public void fallbacksToSecondResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(I18N_RETURNS_ALWAYS_EMPTY, i18nReturnsAlwaysValue("bar"));
        testCompositeResolver(i18nResolverList, message -> assertThat(message).contains("bar"));
    }

    @Test
    public void emptyWhenNotFoundInAnyResolver() throws Exception {
        final List<I18nResolver> i18nResolverList = asList(I18N_RETURNS_ALWAYS_EMPTY, I18N_RETURNS_ALWAYS_EMPTY);
        testCompositeResolver(i18nResolverList, message -> assertThat(message).isEmpty());
    }

    public void testCompositeResolver(final List<I18nResolver> i18nResolverList, final Consumer<Optional<String>> test) {
        final CompositeI18nResolver i18nResolver = CompositeI18nResolver.of(i18nResolverList);
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("bundle", "key");
        final Optional<String> message = i18nResolver.get(singletonList(ENGLISH), i18nIdentifier);
        test.accept(message);
    }

    private I18nResolver i18nReturnsAlwaysValue(final String bar) {
        return (locale, i18nIdentifier, hashArgs) -> Optional.of(bar);
    }

}
