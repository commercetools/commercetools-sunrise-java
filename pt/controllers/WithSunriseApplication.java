package controllers;

import basicauth.BasicAuthTestModule;
import categorytree.CategoryTreeTestModule;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import common.controllers.TestableReverseRouter;
import common.controllers.TestableSphereClient;
import ctpclient.CtpClientTestModule;
import inject.ApplicationTestModule;
import inject.CtpModelsTestModule;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import org.apache.commons.lang3.RandomStringUtils;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Http;
import reverserouter.ReverseRouterTestModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static common.JsonUtils.readCtpObject;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public abstract class WithSunriseApplication {
    public static final int ALLOWED_TIMEOUT = 100000;

    @FunctionalInterface
    public interface CheckedConsumer<T> {

        void apply(T t) throws Exception;
    }

    protected final void run(final Application app, final String url, final CheckedConsumer<WSRequest> test) {
        running(testServer(3333, app), () -> {
            try {
                test.apply(WS.url("http://localhost:3333" + url));
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    protected final <C extends Controller> void run(final Application app, final Class<C> controllerClass,
                                                    final CheckedConsumer<C> test) {
        running(app, () -> {
            try {
                test.apply(app.injector().instanceOf(controllerClass));
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    protected final void setContext(final Http.Request request) {
        Http.Context.current.set(new Http.Context(request));
    }

    protected Http.RequestBuilder requestBuilder() {
        return new Http.RequestBuilder().id(1L);
    }

    protected Application app(final Module ... modules) {
        final Module testModule = Modules.override(testModule()).with(modules);
        return appBuilder().overrides(testModule).build();
    }

    protected GuiceApplicationBuilder appBuilder() {
        return new GuiceApplicationBuilder()
                .in(Environment.simple())
                .loadConfig(testConfiguration());
    }

    protected Module testModule() {
        final List<Category> categories = readCtpObject("data/categories.json", CategoryQuery.resultTypeReference()).getResults();
        return Modules.combine(
                new CtpClientTestModule(TestableSphereClient.ofEmptyResponse()),
                new ApplicationTestModule(),
                new CtpModelsTestModule(),
                new BasicAuthTestModule(null),
                new ReverseRouterTestModule(new TestableReverseRouter()),
                new CategoryTreeTestModule(categories));
    }

    protected Configuration testConfiguration() {
        final Configuration configuration = Configuration.load(new Environment(Mode.TEST));
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.metrics.enabled", false);
        additionalSettings.put("application.settingsWidget.enabled", false);
        additionalSettings.put("play.crypto.secret", RandomStringUtils.randomAlphanumeric(5));
        return new Configuration(additionalSettings).withFallback(configuration);
    }
}