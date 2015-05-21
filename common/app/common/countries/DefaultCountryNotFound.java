package common.countries;

public class DefaultCountryNotFound extends RuntimeException {

    public DefaultCountryNotFound() {
        super("No valid country defined in configuration file");
    }
}
