package controllers;

import plugins.Global;
import play.test.FakeApplication;
import play.test.WithApplication;

import static play.test.Helpers.fakeApplication;

public class WithSunriseApplication extends WithApplication {
    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(new Global() {

        });
    }
}
