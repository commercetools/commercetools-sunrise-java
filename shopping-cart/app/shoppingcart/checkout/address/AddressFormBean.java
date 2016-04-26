package shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import common.models.CountryFormFieldBean;
import common.models.TitleFormFieldBean;
import common.template.i18n.I18nResolver;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.List;

public class AddressFormBean extends Base {

    private TitleFormFieldBean titleFormField;
    private String firstName;
    private String lastName;
    private String streetName;
    private String additionalStreetInfo;
    private String city;
    private String postalCode;
    private CountryFormFieldBean countries;
    private String region;
    private String phone;
    private String email;

    public AddressFormBean() {
    }

    public AddressFormBean(@Nullable final Address address, final List<CountryCode> availableCountries,
                           final UserContext userContext, final I18nResolver i18nResolver, final Configuration configuration) {
        if (address != null) {
            this.titleFormField = new TitleFormFieldBean(address.getTitle(), userContext, i18nResolver, configuration);
            this.firstName = address.getFirstName();
            this.lastName = address.getLastName();
            this.streetName = address.getStreetName();
            this.additionalStreetInfo = address.getAdditionalStreetInfo();
            this.city = address.getCity();
            this.postalCode = address.getPostalCode();
            this.region = address.getRegion();
            this.countries = new CountryFormFieldBean(availableCountries, address.getCountry(), userContext);
            this.phone = address.getPhone();
            this.email = address.getEmail();
        } else {
            this.titleFormField = new TitleFormFieldBean(userContext, i18nResolver, configuration);
            this.countries = new CountryFormFieldBean(availableCountries, userContext.country(), userContext);
        }
    }

    public TitleFormFieldBean getTitleFormField() {
        return titleFormField;
    }

    public void setTitleFormField(final TitleFormFieldBean titleFormField) {
        this.titleFormField = titleFormField;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public CountryFormFieldBean getCountries() {
        return countries;
    }

    public void setCountries(final CountryFormFieldBean countries) {
        this.countries = countries;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
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
