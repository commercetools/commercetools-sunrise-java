package common.template.cms;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CmsIdentifierTest {

    @Test
    public void parsesIdentifier() throws Exception {
        final CmsIdentifier identifier = CmsIdentifier.of("contentType:contentId.some.field");
        assertThat(identifier.getContentType()).isEqualTo("contentType");
        assertThat(identifier.getContentId()).isEqualTo("contentId");
        assertThat(identifier.getField()).isEqualTo("some.field");
    }

    @Test
    public void parsesEmptyIdentifier() throws Exception {
        final CmsIdentifier identifier = CmsIdentifier.of("");
        assertThat(identifier.getContentType()).isEmpty();
        assertThat(identifier.getContentId()).isEmpty();
        assertThat(identifier.getField()).isEmpty();
    }
}
