package purchase;

import io.sphere.sdk.models.Address;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class AddressBean {
    private String title;
    private String salutation;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNumber;
    private String additionalStreetInfo;
    private String city;
    private String region;
    private String postalCode;
    private String countryCode;
    private String country;
    private String phone;
    private String email;

    public AddressBean() {
    }

    public AddressBean(@Nullable final Address address, final Locale locale) {
        if (address != null) {
            try {
                BeanUtils.copyProperties(this, address);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            setCountryCode(address.getCountry().getAlpha2());
            setCountry(address.getCountry().toLocale().getDisplayCountry(locale));
        }
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

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(final String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(final String salutation) {
        this.salutation = salutation;
    }
}
