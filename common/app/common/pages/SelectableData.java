package common.pages;

public class SelectableData {
    private final String text;
    private final String value;
    private final String description;
    private final String image;
    private final boolean selected;

    public SelectableData(final String text, final String value, final String description, final String image, final boolean selected) {
        this.text = text;
        this.value = value;
        this.description = description;
        this.image = image;
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public boolean isSelected() {
        return selected;
    }
}
