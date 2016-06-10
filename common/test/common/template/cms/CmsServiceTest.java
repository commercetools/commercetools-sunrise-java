package common.template.cms;

import com.commercetools.sunrise.common.template.cms.CmsService;
import org.junit.Test;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;

public class CmsServiceTest {

    @Test
    public void getsMessage() throws Exception {
        final CmsService cmsService = ((k, a) -> completedFuture(Optional.of("bar")));
        assertThat(cmsService.getOrEmpty(emptyList(), null).toCompletableFuture().join()).isEqualTo("bar");
    }

    @Test
    public void getsEmptyStringWhenKeyNotFound() throws Exception {
        final CmsService cmsService = ((k, a) -> completedFuture(Optional.empty()));
        assertThat(cmsService.getOrEmpty(emptyList(), null).toCompletableFuture().join()).isEmpty();
    }
}
