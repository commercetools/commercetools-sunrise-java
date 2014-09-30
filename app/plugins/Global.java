package plugins;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.CountryOperations;
import io.sphere.sdk.client.PlayJavaClient;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    private Injector injector;

    @Override
    public void onStart(final Application app) {
        checkDefaultCountry(app);
        super.onStart(app);
        injector = createInjector(app);
    }

    protected Injector createInjector(Application app) {
        return Guice.createInjector(new ProductionModule(app));
    }

    private void checkDefaultCountry(Application app) {
        CountryOperations.of(app.configuration()).defaultCountry();
    }

    @Override
    public void onStop(final Application app) {
        injector.getInstance(PlayJavaClient.class).close();
        super.onStop(app);
    }

    @Override
    public <A> A getControllerInstance(final Class<A> controllerClass) throws Exception {
        return injector.getInstance(controllerClass);
    }

}
