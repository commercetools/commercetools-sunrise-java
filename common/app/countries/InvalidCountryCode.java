package countries;

public class InvalidCountryCode extends IllegalArgumentException {

    public InvalidCountryCode(String countryCodeAsString) {
        super("Invalid country code " + countryCodeAsString);
    }
}
