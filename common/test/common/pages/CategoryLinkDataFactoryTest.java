package common.pages;

import common.categories.CategoryUtils;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryLinkDataFactoryTest {
    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categories.json").getResults());

    @Test
    public void create() {
        final Category category = categories.findById("5ebe6dc9-ba32-4030-9f3e-eee0137a1274").get();

        final LinkData linkData = CategoryLinkDataFactory.of(singletonList(ENGLISH)).create(category);

        assertThat(linkData.getText()).isEqualTo("TestSnowboard equipment");
        assertThat(linkData.getUrl()).isEqualTo("");
    }
}
