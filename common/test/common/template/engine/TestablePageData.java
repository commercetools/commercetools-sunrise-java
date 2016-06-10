package common.template.engine;

import com.commercetools.sunrise.common.controllers.*;

public class TestablePageData implements PageData {

    public String getTitle() {
        return "foo";
    }

    public String getMessage() {
        return "bar";
    }

    @Override
    public PageHeader getHeader() {
        return null;
    }

    @Override
    public PageContent getContent() {
        return null;
    }

    @Override
    public PageFooter getFooter() {
        return null;
    }

    @Override
    public PageMeta getMeta() {
        return null;
    }
}
