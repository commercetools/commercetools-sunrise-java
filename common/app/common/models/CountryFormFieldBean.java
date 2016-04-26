package common.models;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CountryFormFieldBean {

    private List<SelectableBean> list;


    public CountryFormFieldBean() {
    }

    public CountryFormFieldBean(final List<CountryCode> availableCountries, @Nullable final CountryCode selectedCountry,
                                final UserContext userContext) {
        this.list = availableCountries.stream()
                .map(countryOption -> countryToSelectableData(countryOption, selectedCountry, userContext))
                .collect(toList());
    }

    public List<SelectableBean> getList() {
        return list;
    }

    public void setList(final List<SelectableBean> list) {
        this.list = list;
    }

    private SelectableBean countryToSelectableData(final CountryCode country, final @Nullable CountryCode selectedCountry,
                                                   final UserContext userContext) {
        final SelectableBean selectableBean = new SelectableBean();
        selectableBean.setLabel(country.toLocale().getDisplayCountry(userContext.locale()));
        selectableBean.setValue(country.getAlpha2());
        if (country.equals(selectedCountry)) {
            selectableBean.setSelected(true);
        }
        return selectableBean;
    }
}
