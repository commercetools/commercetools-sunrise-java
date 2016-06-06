package common.controllers;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.models.LocationSelector;
import common.models.NavMenuDataFactory;
import common.template.engine.TemplateEngine;
import framework.ControllerComponent;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.FutureUtils;
import play.mvc.Controller;
import play.mvc.Http;
import shoppingcart.CartSessionUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class SunriseFrameworkController extends Controller {
    @Inject
    private SphereClient sphere;
    @Inject
    private UserContext userContext;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private PageMetaFactory pageMetaFactory;
    @Inject
    private NavMenuDataFactory navMenuDataFactory;
    private final List<ControllerComponent> controllerComponents = new LinkedList<>();

    protected SunriseFrameworkController() {
    }

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
        pageHeader.setLocation(new LocationSelector(projectContext, userContext));
        pageHeader.setNavMenu(navMenuDataFactory.create());
        pageHeader.setMiniCart(CartSessionUtils.getMiniCart(session));
//        pageHeader.setCustomerServiceNumber(configuration().getString("checkout.customerServiceNumber"));//TODO framework check
        return new SunrisePageData(pageHeader, new PageFooter(), content, pageMetaFactory.create());
    }

    @Nullable
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        controllerComponents.add(controllerComponent);
    }

    protected final <T> CompletionStage<Object> runAsyncHook(final Class<T> hookClass, final Function<T, CompletionStage<?>> f) {
        //TODO throw a helpful NPE if component returns null instead of CompletionStage
        final List<CompletionStage<Void>> collect = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(hook -> f.apply((T) hook))
                .map(stage -> (CompletionStage<Void>) stage)
                .collect(Collectors.toList());
        final CompletionStage<?> listCompletionStage = FutureUtils.listOfFuturesToFutureOfList(collect);
        return listCompletionStage.thenApply(z -> null);
    }
}
