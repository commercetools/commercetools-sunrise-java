package common.pages;

public interface PageData {

    PageHeader getHeader();

    PageContent getContent();

    PageFooter getFooter();

    SeoData getSeo();
}
