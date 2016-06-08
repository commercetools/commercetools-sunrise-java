package common.controllers;

import com.google.inject.Injector;
import common.contexts.UserContext;
import common.hooks.Hook;
import common.template.engine.TemplateEngine;
import framework.ControllerComponent;
import framework.MultiControllerComponentResolver;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.FutureUtils;
import play.mvc.Controller;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class SunriseFrameworkController extends Controller {
    @Inject
    private SphereClient sphere;
    @Inject
    private UserContext userContext;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private PageMetaFactory pageMetaFactory;

    private final List<ControllerComponent> controllerComponents = new LinkedList<>();


    @Inject
    public void setMultiControllerComponents(final MultiControllerComponentResolver multiComponent, final Injector injector) {
        final List<Class<? extends ControllerComponent>> components = multiComponent.findMatchingComponents(this);
        components.forEach(clazz -> {
            final ControllerComponent instance = injector.getInstance(clazz);
            controllerComponents.add(instance);
        });
    }

    protected SunriseFrameworkController() {
    }

    public abstract Set<String> getFrameworkTags();

    public SphereClient sphere() {
        return sphere;
    }

    public UserContext userContext() {
        return userContext;
    }

    public TemplateEngine templateEngine() {
        return templateEngine;
    }

    protected final SunrisePageData pageData(final UserContext userContext, final PageContent content,
                                             final Http.Context ctx, final Http.Session session) {
        final PageHeader pageHeader = new PageHeader(content.getAdditionalTitle());
        return new SunrisePageData(pageHeader, new PageFooter(), content, pageMetaFactory.create());
    }

    @Nullable
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        controllerComponents.add(controllerComponent);
    }

    protected final <T extends Hook> void runVoidHook(final Class<T> hookClass, final Consumer<T> consumer) {
        controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .forEach(action -> consumer.accept((T) action));
    }

    protected final <T extends Hook, R> R runFilterHook(final Class<T> hookClass, final BiFunction<T, R, R> f, final R param) {
        R result = param;
        final List<T> applyableHooks = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(x -> (T) x)
                .collect(Collectors.toList());
        for (final T hook : applyableHooks) {
            result = f.apply(hook, result);
        }
        return result;
    }

    protected final <T extends Hook> CompletionStage<Object> runAsyncHook(final Class<T> hookClass, final Function<T, CompletionStage<?>> f) {
        //TODO throw a helpful NPE if component returns null instead of CompletionStage
        final List<CompletionStage<Void>> collect = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(hook -> f.apply((T) hook))
                .map(stage -> (CompletionStage<Void>) stage)
                .collect(toList());
        final CompletionStage<?> listCompletionStage = FutureUtils.listOfFuturesToFutureOfList(collect);
        return listCompletionStage.thenApply(z -> null);
    }
}
