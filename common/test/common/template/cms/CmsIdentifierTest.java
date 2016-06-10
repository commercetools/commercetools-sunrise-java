package common.template.cms;

import com.commercetools.sunrise.common.template.cms.CmsIdentifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CmsIdentifierTest {

    @Test
    public void parsesIdentifier() throws Exception {
        final CmsIdentifier identifier = CmsIdentifier.of("entryType:entryKey.some.field");
        assertThat(identifier.getEntryType()).isEqualTo("entryType");
        assertThat(identifier.getEntryKey()).isEqualTo("entryKey");
        assertThat(identifier.getFieldName()).isEqualTo("some.field");
    }

    @Test
    public void parsesEmptyIdentifier() throws Exception {
        final CmsIdentifier identifier = CmsIdentifier.of("");
        assertThat(identifier.getEntryType()).isEmpty();
        assertThat(identifier.getEntryKey()).isEmpty();
        assertThat(identifier.getFieldName()).isEmpty();
    }
}
