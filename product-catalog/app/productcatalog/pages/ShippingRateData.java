package productcatalog.pages;

public class ShippingRateData {

    private final String shippingMethod;
    private final String zone;
    private final String shippingRate;
    private final String freeAbove;

    public ShippingRateData(final String shippingMethod, final String zone, final String shippingRate, final String freeAbove) {
        this. shippingMethod = shippingMethod;
        this.zone = zone;
        this.shippingRate = shippingRate;
        this.freeAbove = freeAbove;
    }

    public String getText() {
        return String.format("%s %s %s %s", shippingMethod, zone, shippingRate, freeAbove);
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public String getZone() {
        return zone;
    }

    public String getShippingRate() {
        return shippingRate;
    }

    public String getFreeAbove() {
        return freeAbove;
    }
}
