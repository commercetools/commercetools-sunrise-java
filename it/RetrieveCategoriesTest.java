import io.sphere.sdk.categories.requests.CategoryQuery;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.fest.assertions.Assertions.assertThat;

public class RetrieveCategoriesTest extends WithSphereJavaClient {
    @Test
    public void demo() throws ExecutionException, InterruptedException {
        final int count = sphereJavaClient.execute(new CategoryQuery()).get().getCount();
        assertThat(count).isGreaterThan(3);
    }
}
