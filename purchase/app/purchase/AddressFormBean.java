package purchase;

import common.contexts.UserContext;
import io.sphere.sdk.models.Address;
import play.Configuration;
import play.i18n.Messages;

import javax.annotation.Nullable;

public class AddressFormBean {
    private SalutationsFieldsBean salutations;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNumber;
    private String city;
    private String region;
    private String postalCode;
    private CountriesFieldsBean countries;
    private String phone;
    private String email;

    public AddressFormBean() {
    }

    public AddressFormBean(@Nullable final Address address, final UserContext userContext, final Messages messages, final Configuration configuration) {
        final SalutationsFieldsBean salutationsFieldsBean = new SalutationsFieldsBean(address, userContext, messages, configuration);
        setSalutations(salutationsFieldsBean);
        final CountriesFieldsBean countriesFieldsBean = new CountriesFieldsBean(address, userContext, messages, configuration);
        setCountries(countriesFieldsBean);
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public CountriesFieldsBean getCountries() {
        return countries;
    }

    public void setCountries(final CountriesFieldsBean countries) {
        this.countries = countries;
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

    public SalutationsFieldsBean getSalutations() {
        return salutations;
    }

    public void setSalutations(final SalutationsFieldsBean salutations) {
        this.salutations = salutations;
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
}
