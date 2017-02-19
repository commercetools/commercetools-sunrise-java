package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionAddressBookReverseRouter extends AbstractReflectionReverseRouter implements AddressBookSimpleReverseRouter {

    private final ReverseCaller addressBookDetailPageCaller;
    private final ReverseCaller addAddressPageCaller;
    private final ReverseCaller addAddressProcessCaller;
    private final ReverseCaller changeAddressPageCaller;
    private final ReverseCaller changeAddressProcessCaller;
    private final ReverseCaller removeAddressProcessCaller;

    @Inject
    private ReflectionAddressBookReverseRouter(final ParsedRouteList parsedRouteList) {
        addressBookDetailPageCaller = getCallerForRoute(parsedRouteList, ADDRESS_BOOK_DETAIL_PAGE);
        addAddressPageCaller = getCallerForRoute(parsedRouteList, ADD_ADDRESS_PAGE);
        addAddressProcessCaller = getCallerForRoute(parsedRouteList, ADD_ADDRESS_PROCESS);
        changeAddressPageCaller = getCallerForRoute(parsedRouteList, CHANGE_ADDRESS_PAGE);
        changeAddressProcessCaller = getCallerForRoute(parsedRouteList, CHANGE_ADDRESS_PROCESS);
        removeAddressProcessCaller = getCallerForRoute(parsedRouteList, REMOVE_ADDRESS_PROCESS);
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
    public Call changeAddressPageCall(final String languageTag, final String addressId) {
        return changeAddressPageCaller.call(languageTag, addressId);
    }

    @Override
    public Call changeAddressProcessCall(final String languageTag, final String addressId) {
        return changeAddressProcessCaller.call(languageTag, addressId);
    }

    @Override
    public Call removeAddressProcessCall(final String languageTag, final String addressId) {
        return removeAddressProcessCaller.call(languageTag, addressId);
    }
}
