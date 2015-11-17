package common.pages;

import io.sphere.sdk.models.Base;

public class SelectableData extends Base {
    private String text;
    private String value;
    private String description;
    private String image;
    private boolean selected;

    public SelectableData() {
    }

    public SelectableData(final String text, final String value, final String description, final String image, final boolean selected) {
        this.text = text;
        this.value = value;
        this.description = description;
        this.image = image;
        this.selected = selected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
