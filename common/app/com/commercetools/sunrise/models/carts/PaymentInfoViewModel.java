package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import io.sphere.sdk.models.LocalizedString;

public class PaymentInfoViewModel extends ViewModel {

    private LocalizedString type;

    public PaymentInfoViewModel() {
    }

    public LocalizedString getType() {
        return type;
    }

    public void setType(final LocalizedString type) {
        this.type = type;
    }
}
