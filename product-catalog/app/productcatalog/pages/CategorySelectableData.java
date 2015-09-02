package productcatalog.pages;

import common.pages.SelectableData;

import java.util.List;

public class CategorySelectableData extends SelectableData {
    private final List<SelectableData> children;

    public CategorySelectableData(final String text, final String value, final String description, final String image,
                                  final boolean selected, final List<SelectableData> children) {
        super(text, value, description, image, selected);
        this.children = children;
    }

    public List<SelectableData> getChildren() {
        return children;
    }
}
