package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionAddressBookReverseRouter extends ReflectionReverseRouterBase implements AddressBookReverseRouter {

    private final ReverseCaller addressBookCaller;
    private final ReverseCaller addAddressToAddressBookCaller;
    private final ReverseCaller addAddressToAddressBookProcessFormCaller;
    private final ReverseCaller changeAddressInAddressBookCaller;
    private final ReverseCaller changeAddressInAddressBookProcessFormCaller;
    private final ReverseCaller removeAddressFromAddressBookProcessFormCaller;

    @Inject
    private ReflectionAddressBookReverseRouter(final ParsedRoutes parsedRoutes) {
        addressBookCaller = getCallerForRoute(parsedRoutes, "addressBookCall");
        addAddressToAddressBookCaller = getCallerForRoute(parsedRoutes, "addAddressToAddressBookCall");
        addAddressToAddressBookProcessFormCaller = getCallerForRoute(parsedRoutes, "addAddressToAddressBookProcessFormCall");
        changeAddressInAddressBookCaller = getCallerForRoute(parsedRoutes, "changeAddressInAddressBookCall");
        changeAddressInAddressBookProcessFormCaller = getCallerForRoute(parsedRoutes, "changeAddressInAddressBookProcessFormCall");
        removeAddressFromAddressBookProcessFormCaller = getCallerForRoute(parsedRoutes, "removeAddressFromAddressBookProcessFormCall");
    }

    @Override
    public Call addressBookCall(final String languageTag) {
        return addressBookCaller.call(languageTag);
    }

    @Override
    public Call addAddressToAddressBookCall(final String languageTag) {
        return addAddressToAddressBookCaller.call(languageTag);
    }

    @Override
    public Call addAddressToAddressBookProcessFormCall(final String languageTag) {
        return addAddressToAddressBookProcessFormCaller.call(languageTag);
    }

    @Override
    public Call changeAddressInAddressBookCall(final String languageTag, final String addressId) {
        return changeAddressInAddressBookCaller.call(languageTag, addressId);
    }

    @Override
    public Call changeAddressInAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return changeAddressInAddressBookProcessFormCaller.call(languageTag, addressId);
    }

    @Override
    public Call removeAddressFromAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return removeAddressFromAddressBookProcessFormCaller.call(languageTag, addressId);
    }
}
