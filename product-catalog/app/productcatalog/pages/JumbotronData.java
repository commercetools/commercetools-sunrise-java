package productcatalog.pages;

public class JumbotronData {
    private final String category;
    private final String text;
    private final String description;

    public JumbotronData(final String category, final String text, final String description) {
        this.category = category;
        this.text = text;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }
}
