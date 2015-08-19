package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class TermUiDataTest {

    @Test
    public void buildsTerm() throws Exception {
        final TermStats<String> termStats = TermStats.of("bar", 5);
        final TermUiData<String> term = TermUiData.of(termStats, asList("foo", "bar"));
        assertThat(term.getTerm()).isEqualTo("bar");
        assertThat(term.getCount()).isEqualTo(5);
        assertThat(term.isSelected()).isTrue();
    }

    @Test
    public void isNotSelectedWhenSelectedValuesDoNotContainTerm() throws Exception {
        final TermStats<String> termStats = TermStats.of("qux", 5);
        final TermUiData<String> term = TermUiData.of(termStats, asList("foo", "bar"));
        assertThat(term.isSelected()).isFalse();
    }
}
