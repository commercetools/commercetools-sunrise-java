package shoppingcart;

import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.Locale;

public class AddressBean extends Base {

    private String title;
    private String firstName;
    private String lastName;
    private String streetName;
    private String additionalStreetInfo;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String phone;
    private String email;

    public AddressBean() {
    }

    public AddressBean(@Nullable final Address address, final Locale locale) {
        if (address != null) {
            this.title = address.getTitle();
            this.firstName = address.getFirstName();
            this.lastName = address.getLastName();
            this.streetName = address.getStreetName();
            this.additionalStreetInfo = address.getAdditionalStreetInfo();
            this.city = address.getCity();
            this.region = address.getRegion();
            this.postalCode = address.getPostalCode();
            this.country = address.getCountry().toLocale().getDisplayCountry(locale);
            this.phone = address.getPhone();
            this.email = address.getEmail();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    public String getAdditionalStreetInfo() {
        return additionalStreetInfo;
    }

    public void setAdditionalStreetInfo(final String additionalStreetInfo) {
        this.additionalStreetInfo = additionalStreetInfo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
