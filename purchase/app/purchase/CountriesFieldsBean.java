package purchase;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.models.SelectableData;
import io.sphere.sdk.models.Address;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class CountriesFieldsBean {
    private List<SelectableData> list;


    public CountriesFieldsBean() {
    }

    public CountriesFieldsBean(@Nullable final Address address, final UserContext userContext, final ProjectContext projectContext) {
        final String selectedCountry = Optional.ofNullable(address).map(Address::getCountry).map(CountryCode::getAlpha2).orElse(null);
        fill(userContext, projectContext, selectedCountry);
    }

    private void fill(final UserContext userContext, final ProjectContext projectContext, final String selectedCountry) {
        final List<SelectableData> selectableDataList = projectContext.countries().stream().map(countryCode -> {
            final SelectableData selectableData = new SelectableData();
            selectableData.setLabel(countryCode.toLocale().getDisplayCountry(userContext.locale()));
            selectableData.setValue(countryCode.getAlpha2());
            selectableData.setSelected(countryCode.getAlpha2().equals(selectedCountry));
            return selectableData;
        }).collect(toList());
        setList(selectableDataList);
    }

    public CountriesFieldsBean(@Nullable final String country, final UserContext userContext, final ProjectContext projectContext) {
        fill(userContext, projectContext, country);
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
