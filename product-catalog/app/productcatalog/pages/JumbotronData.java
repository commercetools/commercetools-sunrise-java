package productcatalog.pages;

import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;

import java.util.Optional;

public class JumbotronData extends Base {
    private String title;
    private String subtitle;
    private String description;

    public JumbotronData() {
    }

    public JumbotronData(final Category category, final UserContext userContext, final CategoryTree categoryTree) {
        this.title = category.getName().find(userContext.locales()).orElse("");
        Optional.ofNullable(category.getParent())
                .ifPresent(parentRef -> categoryTree.findById(parentRef.getId())
                        .ifPresent(parent -> this.subtitle = parent.getName().find(userContext.locales()).orElse("")));
        Optional.ofNullable(category.getDescription())
                .ifPresent(description -> this.description = description.find(userContext.locales()).orElse(""));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
