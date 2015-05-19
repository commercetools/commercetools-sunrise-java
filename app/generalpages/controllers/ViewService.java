package generalpages.controllers;

import models.PopPageData;
import play.twirl.api.Html;

public interface ViewService {

    Html popPage(PopPageData data);

    static ViewService of() {
        return new HandlebarsViewService(TemplateService.of());
    }
}
