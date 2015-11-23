package purchase;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import common.models.SelectableData;
import io.sphere.sdk.models.Address;
import play.Configuration;
import play.i18n.Messages;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CountriesFieldsBean {
    private List<SelectableData> list;


    public CountriesFieldsBean() {
    }

    public CountriesFieldsBean(@Nullable final Address address, final UserContext userContext, final Messages messages, final Configuration configuration) {
        final String selectedCountry = Optional.ofNullable(address).map(Address::getCountry).map(CountryCode::getAlpha2).orElse(null);
        fill(userContext, configuration, selectedCountry);
    }

    private void fill(final UserContext userContext, final Configuration configuration, final String selectedCountry) {
        final List<SelectableData> selectableDataList = configuration.getStringList("checkout.allowedCountries").stream().map(countryCode -> {
            final SelectableData selectableData = new SelectableData();
            selectableData.setLabel(CountryCode.valueOf(countryCode).toLocale().getDisplayCountry(userContext.locale()));
            selectableData.setValue(countryCode);
            selectableData.setSelected(countryCode.equals(selectedCountry));
            return selectableData;
        }).collect(toList());
        setList(selectableDataList);
    }

    public CountriesFieldsBean(@Nullable final String country, final UserContext userContext, final Configuration configuration) {
        fill(userContext, configuration, country);
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
