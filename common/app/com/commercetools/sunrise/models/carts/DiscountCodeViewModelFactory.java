package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.viewmodels.SimpleViewModelFactory;
import com.google.inject.Inject;
import io.sphere.sdk.discountcodes.DiscountCode;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DiscountCodeViewModelFactory extends SimpleViewModelFactory<DiscountCodeViewModel, DiscountCode> {

    private final I18nResolver i18nResolver;

    @Inject
    DiscountCodeViewModelFactory(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
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
                .flatMap(i18nResolver::get)
                .orElseGet(discountCode::getCode);
        viewModel.setName(displayName);
    }

    protected void fillDescription(final DiscountCodeViewModel viewModel, final DiscountCode discountCode) {
        viewModel.setDescription(discountCode.getDescription());
    }
}
