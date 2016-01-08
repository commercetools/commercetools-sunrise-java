package purchase;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.Base;
import org.apache.commons.beanutils.BeanUtils;
import play.Configuration;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public class AddressFormBean extends Base {
    private SalutationsFieldsBean salutations;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNumber;
    private String additionalStreetInfo;
    private String city;
    private String region;
    private String postalCode;
    private CountriesFieldsBean countries;
    private String phone;
    private String email;

    public AddressFormBean() {
    }

    public AddressFormBean(@Nullable final Address address, final UserContext userContext, final ProjectContext projectContext,
                           final I18nResolver i18nResolver, final Configuration configuration) {
        if (address != null) {
            try {
                BeanUtils.copyProperties(this, address);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        final SalutationsFieldsBean salutationsFieldsBean = new SalutationsFieldsBean(address, userContext, i18nResolver, configuration);
        setSalutations(salutationsFieldsBean);
        final CountriesFieldsBean countriesFieldsBean = new CountriesFieldsBean(address, userContext, projectContext);
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

    public String getAdditionalStreetInfo() {
        return additionalStreetInfo;
    }

    public void setAdditionalStreetInfo(final String additionalStreetInfo) {
        this.additionalStreetInfo = additionalStreetInfo;
    }

    public Address toAddress() {
        return AddressBuilder.of(CountryCode.valueOf(getCountries().getSelectedCountryCode()))
                .salutation(getSalutations().getSelected())
                .firstName(getFirstName())
                .lastName(getLastName())
                .streetName(getStreetName())
                .streetNumber(getStreetNumber())
                .additionalStreetInfo(getAdditionalStreetInfo())
                .city(getCity())
                .region(getRegion())
                .postalCode(getPostalCode())
                .phone(getPhone())
                .email(getEmail())
                .build();
    }
}
