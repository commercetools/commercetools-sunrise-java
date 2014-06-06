package fixtures;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryImpl;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.CategoryTreeFactory;
import io.sphere.sdk.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class CategoryFixtures {
    private CategoryFixtures() {
    }

    //TODO this is maybe helpful for a testing library
    //TODO make generic
    private static List<Category> loadCategories(String resourcePath) {
        final URL url = Resources.getResource(resourcePath);
        try {
            String categoriesJson = Resources.toString(url, Charsets.UTF_8);
            return JsonUtils.newObjectMapper().<List<Category>>readValue(categoriesJson, new TypeReference<List<CategoryImpl>>() {

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CategoryTree categoryTree() {
        return CategoryTreeFactory.create(loadCategories("categories.json"));
    }
}
