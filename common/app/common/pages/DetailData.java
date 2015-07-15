package common.pages;

public class DetailData {

    private final String text;
    private final String description;

    public DetailData(final String text, final String description) {
        this.text = text;
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }
}
