package controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import play.libs.F;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.routes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Locale;
import java.util.stream.Collectors;

@Singleton
public class TmpPopController extends SunriseController {

    @Inject
    public TmpPopController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> show() {
        final ProductProjectionQuery query = ProductProjectionQuery.ofCurrent()
                .withPredicates(product -> product.name().lang(Locale.ENGLISH).isPresent())
                .plusPredicates(product -> product.name().lang(Locale.GERMAN).isPresent())
                .withLimit(100)
                .withSort(product -> product.name().lang(Locale.ENGLISH).sort().desc());
        return sphere().execute(query).map(pagedResult -> {
            final String links = pagedResult.getResults().stream()
                    .filter(p -> p.getSlug().get(Locale.ENGLISH) != null)
                    .map(p -> {
                        final String allVariantsLink = p.getAllVariants().stream()
                                .map((ProductVariant v) -> v.getSku())
                                .filter(sku -> sku != null)
                                .map(sku -> {
                                    final String url = routes.ProductDetailPageController.show("en", p.getSlug().get(Locale.ENGLISH), sku).url();
                                    return String.format("<a href=\"%s\">%s</a>  ", url, sku);
                                })
                                .collect(Collectors.joining());

                        return String.format("<p>%s %s</p>", p.getName().get(Locale.ENGLISH), allVariantsLink);
                    }).collect(Collectors.joining());
            return ok(new Html(links));
        });
    }
}
