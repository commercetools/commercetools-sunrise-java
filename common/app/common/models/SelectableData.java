package common.models;

import io.sphere.sdk.models.Base;

public class SelectableData extends Base {
    private String label;
    private String value;
    private String description;
    private String image;
    private boolean selected;

    public SelectableData() {
    }

    public SelectableData(final String label, final String value) {
        this.label = label;
        this.value = value;
    }

    public SelectableData(final String label, final String value, final boolean selected) {
        this.label = label;
        this.value = value;
        this.selected = selected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
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
}
