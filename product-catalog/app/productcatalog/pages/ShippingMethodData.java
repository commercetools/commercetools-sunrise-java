package productcatalog.pages;

public class ShippingMethodData {
    private String name;
    private boolean freeAbove;
    private String price;
    private String description;

    public ShippingMethodData() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isFreeAbove() {
        return freeAbove;
    }

    public void setFreeAbove(final boolean freeAbove) {
        this.freeAbove = freeAbove;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
