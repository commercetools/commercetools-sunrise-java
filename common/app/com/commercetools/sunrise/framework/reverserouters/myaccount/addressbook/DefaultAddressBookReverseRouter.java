package com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import io.sphere.sdk.models.Address;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DefaultAddressBookReverseRouter extends AbstractReflectionReverseRouter implements AddressBookReverseRouter {

    private final ReverseCaller addressBookDetailPageCaller;
    private final ReverseCaller addAddressPageCaller;
    private final ReverseCaller addAddressProcessCaller;
    private final ReverseCaller changeAddressPageCaller;
    private final ReverseCaller changeAddressProcessCaller;
    private final ReverseCaller removeAddressProcessCaller;

    @Inject
    protected DefaultAddressBookReverseRouter(final ParsedRoutes parsedRoutes) {
        addressBookDetailPageCaller = getReverseCallerForSunriseRoute(ADDRESS_BOOK_DETAIL_PAGE, parsedRoutes);
        addAddressPageCaller = getReverseCallerForSunriseRoute(ADD_ADDRESS_PAGE, parsedRoutes);
        addAddressProcessCaller = getReverseCallerForSunriseRoute(ADD_ADDRESS_PROCESS, parsedRoutes);
        changeAddressPageCaller = getReverseCallerForSunriseRoute(CHANGE_ADDRESS_PAGE, parsedRoutes);
        changeAddressProcessCaller = getReverseCallerForSunriseRoute(CHANGE_ADDRESS_PROCESS, parsedRoutes);
        removeAddressProcessCaller = getReverseCallerForSunriseRoute(REMOVE_ADDRESS_PROCESS, parsedRoutes);
    }

    @Override
    public Call addressBookDetailPageCall() {
        return addressBookDetailPageCaller.call();
    }

    @Override
    public Call addAddressPageCall() {
        return addAddressPageCaller.call();
    }

    @Override
    public Call addAddressProcessCall() {
        return addAddressProcessCaller.call();
    }

    @Override
    public Call changeAddressPageCall(final String addressIdentifier) {
        return changeAddressPageCaller.call(addressIdentifier);
    }

    @Override
    public Call changeAddressProcessCall(final String addressIdentifier) {
        return changeAddressProcessCaller.call(addressIdentifier);
    }

    @Override
    public Call removeAddressProcessCall(final String addressIdentifier) {
        return removeAddressProcessCaller.call(addressIdentifier);
    }

    /**
     * {@inheritDoc}
     * It uses as address identifier the address ID.
     */
    @Override
    public Optional<Call> changeAddressPageCall(final Address address) {
        return Optional.of(changeAddressPageCall(address.getId()));
    }

    /**
     * {@inheritDoc}
     * It uses as address identifier the address ID.
     */
    @Override
    public Optional<Call> changeAddressProcessCall(final Address address) {
        return Optional.of(changeAddressProcessCall(address.getId()));
    }

    /**
     * {@inheritDoc}
     * It uses as address identifier the address ID.
     */
    @Override
    public Optional<Call> removeAddressProcessCall(final Address address) {
        return Optional.of(removeAddressProcessCall(address.getId()));
    }
}
