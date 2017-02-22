package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.ViewModel;
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
