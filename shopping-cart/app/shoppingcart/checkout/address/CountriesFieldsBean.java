package shoppingcart.checkout.address;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.models.SelectableData;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class CountriesFieldsBean {
    private List<SelectableData> list;


    public CountriesFieldsBean() {
    }

    public CountriesFieldsBean(@Nullable final String countryCode, final UserContext userContext) {
        final CountryCode selectedCountry = selectedCountry(countryCode, userContext);
        final SelectableData selectableData = selectableDataFromCountry(selectedCountry, true, userContext.locale());
        setList(singletonList(selectableData));
    }

    public CountriesFieldsBean(@Nullable final String countryCode, final UserContext userContext, final ProjectContext projectContext) {
        final CountryCode country = selectedCountry(countryCode, userContext);
        fill(country, userContext, projectContext);
    }

    public CountriesFieldsBean(@Nullable final Address address, final UserContext userContext) {
        final CountryCode selectedCountry = selectedCountry(address, userContext);
        final SelectableData selectableData = selectableDataFromCountry(selectedCountry, true, userContext.locale());
        setList(singletonList(selectableData));
    }

    public CountriesFieldsBean(@Nullable final Address address, final UserContext userContext, final ProjectContext projectContext) {
        final CountryCode selectedCountry = selectedCountry(address, userContext);
        fill(selectedCountry, userContext, projectContext);
    }

    private void fill(final CountryCode selectedCountry, final UserContext userContext, final ProjectContext projectContext) {
        final List<SelectableData> selectableDataList = projectContext.countries().stream().map(country -> {
            final boolean isSelected = country.equals(selectedCountry);
            return selectableDataFromCountry(country, isSelected, userContext.locale());
        }).collect(toList());
        setList(selectableDataList);
    }

    private CountryCode selectedCountry(final @Nullable String countryCode, final UserContext userContext) {
        return Optional.ofNullable(countryCode)
                .map(CountryCode::getByCode)
                .orElse(userContext.country());
    }

    private CountryCode selectedCountry(final @Nullable Address address, final UserContext userContext) {
        return Optional.ofNullable(address)
                .map(Address::getCountry)
                .orElse(userContext.country());
    }

    private SelectableData selectableDataFromCountry(final CountryCode countryCode, final boolean isSelected, final Locale locale) {
        final SelectableData selectableData = new SelectableData();
        selectableData.setLabel(countryCode.toLocale().getDisplayCountry(locale));
        selectableData.setValue(countryCode.getAlpha2());
        selectableData.setSelected(isSelected);
        return selectableData;
    }


    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }

    public String getSelectedCountryCode() {
        return getList().stream()
                .filter(c -> c.isSelected())
                .map(c -> c.getValue())
                .findFirst()
                .orElse(null);
    }
}
