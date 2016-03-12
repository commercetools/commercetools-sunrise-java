package common.cms;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CmsContentTest {

    @Test
    public void getsMessage() throws Exception {
        final CmsContent cmsContent = ((k, a) -> Optional.of("bar"));
        assertThat(cmsContent.getOrEmpty("foo")).isEqualTo("bar");
    }

    @Test
    public void getsEmptyStringWhenKeyNotFound() throws Exception {
        final CmsContent cmsContent = ((k, a) -> Optional.empty());
        assertThat(cmsContent.getOrEmpty("foo")).isEmpty();
    }
}
