package plugins;

import controllers.ApplicationController;
import io.sphere.sdk.categories.Categories;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.CategoryTreeFactory;
import io.sphere.sdk.client.PagedQueryResult;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.client.PlayJavaClientImpl;
import play.Application;
import play.GlobalSettings;

import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {
    private Application app;

    //TODO this will be most likely moved to a plugin
    private PlayJavaClient client;
    private CategoryTree categoryTree;

    @Override
    public void onStart(final Application app) {
        this.app = app;
        client = new PlayJavaClientImpl(app.configuration());
        final PagedQueryResult<Category> queryResult = client.execute(Categories.query()).get(2000, TimeUnit.MILLISECONDS);//TODO this will be most likely moved to a plugin
        //TODO this does not take all categories if there are a lot.
        categoryTree = CategoryTreeFactory.create(queryResult.getResults());
        super.onStart(app);
    }

    @Override
    public void onStop(final Application app) {
        client.close();
        super.onStop(app);
    }

    @Override
    public <A> A getControllerInstance(final Class<A> controllerClass) throws Exception {
        final A result;
        if (controllerClass.equals(ApplicationController.class)) {
            result = (A) new ApplicationController(client, categoryTree);//TODO cache this instance
        } else {
            result = super.getControllerInstance(controllerClass);
        }
        return result;
    }
}
