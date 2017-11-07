package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.localization.UserLanguage;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.google.inject.Inject;
import io.sphere.sdk.discountcodes.DiscountCode;

import java.util.Optional;

@RequestScoped
public class DiscountCodeViewModelFactory extends SimpleViewModelFactory<DiscountCodeViewModel, DiscountCode> {
    private final UserLanguage userLanguage;

    @Inject
    public DiscountCodeViewModelFactory(final UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    protected final UserLanguage getUserLanguage() {
        return userLanguage;
    }

    @Override
    protected DiscountCodeViewModel newViewModelInstance(final DiscountCode discountCode) {
        return new DiscountCodeViewModel();
    }

    @Override
    protected void initialize(final DiscountCodeViewModel viewModel, final DiscountCode discountCode) {
        fillId(viewModel, discountCode);
        fillName(viewModel, discountCode);
        fillDescription(viewModel, discountCode);
    }

    protected void fillId(final DiscountCodeViewModel viewModel, final DiscountCode discountCode) {
        viewModel.setId(discountCode.getId());
    }

    protected void fillName(final DiscountCodeViewModel viewModel, final DiscountCode discountCode) {
        final String displayName = Optional.ofNullable(discountCode.getName())
                .flatMap(name -> name.find(userLanguage.locales()))
                .orElseGet(() -> discountCode.getCode());
        viewModel.setName(displayName);
    }

    protected void fillDescription(final DiscountCodeViewModel viewModel, final DiscountCode discountCode) {
        viewModel.setDescription(discountCode.getDescription());
    }
}
