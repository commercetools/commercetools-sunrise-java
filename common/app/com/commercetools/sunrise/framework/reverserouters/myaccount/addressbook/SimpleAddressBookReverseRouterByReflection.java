package com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleAddressBookReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleAddressBookReverseRouter {

    private final ReverseCaller addressBookDetailPageCaller;
    private final ReverseCaller addAddressPageCaller;
    private final ReverseCaller addAddressProcessCaller;
    private final ReverseCaller changeAddressPageCaller;
    private final ReverseCaller changeAddressProcessCaller;
    private final ReverseCaller removeAddressProcessCaller;

    @Inject
    private SimpleAddressBookReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        addressBookDetailPageCaller = getReverseCallerForSunriseRoute(ADDRESS_BOOK_DETAIL_PAGE, parsedRoutes);
        addAddressPageCaller = getReverseCallerForSunriseRoute(ADD_ADDRESS_PAGE, parsedRoutes);
        addAddressProcessCaller = getReverseCallerForSunriseRoute(ADD_ADDRESS_PROCESS, parsedRoutes);
        changeAddressPageCaller = getReverseCallerForSunriseRoute(CHANGE_ADDRESS_PAGE, parsedRoutes);
        changeAddressProcessCaller = getReverseCallerForSunriseRoute(CHANGE_ADDRESS_PROCESS, parsedRoutes);
        removeAddressProcessCaller = getReverseCallerForSunriseRoute(REMOVE_ADDRESS_PROCESS, parsedRoutes);
    }

    @Override
    public Call addressBookDetailPageCall(final String languageTag) {
        return addressBookDetailPageCaller.call(languageTag);
    }

    @Override
    public Call addAddressPageCall(final String languageTag) {
        return addAddressPageCaller.call(languageTag);
    }

    @Override
    public Call addAddressProcessCall(final String languageTag) {
        return addAddressProcessCaller.call(languageTag);
    }

    @Override
    public Call changeAddressPageCall(final String languageTag, final String addressIdentifier) {
        return changeAddressPageCaller.call(languageTag, addressIdentifier);
    }

    @Override
    public Call changeAddressProcessCall(final String languageTag, final String addressIdentifier) {
        return changeAddressProcessCaller.call(languageTag, addressIdentifier);
    }

    @Override
    public Call removeAddressProcessCall(final String languageTag, final String addressIdentifier) {
        return removeAddressProcessCaller.call(languageTag, addressIdentifier);
    }
}
