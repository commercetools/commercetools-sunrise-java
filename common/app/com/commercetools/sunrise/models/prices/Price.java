package com.commercetools.sunrise.models.prices;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

import javax.money.MonetaryAmount;

public class Price extends ViewModel {

    private final MonetaryAmount current;
    private MonetaryAmount previous;

    public Price(final MonetaryAmount current) {
        this.current = current;
    }

    public MonetaryAmount getCurrent() {
        return current;
    }

    public MonetaryAmount getPrevious() {
        return previous;
    }

    public void setPrevious(final MonetaryAmount previous) {
        this.previous = previous;
    }
}
