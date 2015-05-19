package models;

import java.util.List;

public class PopPageData extends PageData {
    public String title;
    public List<Breadcrumb> breadcrumb;

    public PopPageData(final String title, final List<Breadcrumb> breadcrumb) {
        this.title = title;
        this.breadcrumb = breadcrumb;
    }

    public String getTitle() {
        return title;
    }

    public List<Breadcrumb> getBreadcrumb() {
        return breadcrumb;
    }
}
