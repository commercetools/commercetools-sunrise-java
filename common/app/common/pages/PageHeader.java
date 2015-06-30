package common.pages;

import common.cms.CmsPage;
import common.contexts.ProjectContext;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PageHeader {
    private final CmsPage cms;
    private final ProjectContext projectContext;
    private final String title;

    public PageHeader(final CmsPage cms, final ProjectContext projectContext, final String title) {
        this.cms = cms;
        this.projectContext = projectContext;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public LinkData getStores() {
        return new LinkData(cms.getOrEmpty("header.stores"), "");
    }

    public LinkData getHelp() {
        return new LinkData(cms.getOrEmpty("header.help"), "");
    }

    public LinkData getCallUs() {
        return new LinkData(cms.getOrEmpty("header.callUs"), "");
    }

    public CollectionData getCountries() {
        final List<SelectableData> countries = projectContext.countries().stream().map(country -> {
            final String name = country.getName();
            final String value = country.getAlpha2();
            final String currency = country.getCurrency().getCurrencyCode();
            final String image = String.format("assets/img/flags/%s.svg", value.toLowerCase());
            return new SelectableData(name, value, currency, image, false);
        }).collect(toList());
        return new CollectionData(cms.getOrEmpty("header.countries"), countries);
    }
}
