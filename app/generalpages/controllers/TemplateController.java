package generalpages.controllers;

import models.Breadcrumb;
import models.PopPageData;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

import static java.util.Arrays.asList;

public class TemplateController extends Controller {
    private final ViewService viewService;


    public TemplateController(ViewService viewService) {
        this.viewService = viewService;
    }

    public Result foo() {
        return ok(viewService.popPage(popPageData()));
    }

    private static PopPageData popPageData() {
        return new PopPageData("Sunrise title", breadcrumbs());
    }

    private static List<Breadcrumb> breadcrumbs() {
        return asList(
                new Breadcrumb("home/", "Home", false),
                new Breadcrumb("women/", "Women", false),
                new Breadcrumb("coats/", "Coats", true)
        );
    }
}
