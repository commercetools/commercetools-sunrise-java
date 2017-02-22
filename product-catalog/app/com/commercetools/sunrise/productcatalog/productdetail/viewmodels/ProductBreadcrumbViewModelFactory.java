package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.AbstractBreadcrumbViewModelFactory;
import com.commercetools.sunrise.common.models.BreadcrumbLinkViewModel;
import com.commercetools.sunrise.common.models.BreadcrumbViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestScoped
public class ProductBreadcrumbViewModelFactory extends AbstractBreadcrumbViewModelFactory<ProductWithVariant> {

    @Inject
    public ProductBreadcrumbViewModelFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        super(categoryTree, productReverseRouter);
    }

    @Override
    public final BreadcrumbViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected BreadcrumbViewModel getViewModelInstance() {
        return new BreadcrumbViewModel();
    }

    @Override
    protected final void initialize(final BreadcrumbViewModel model, final ProductWithVariant productWithVariant) {
        super.initialize(model, productWithVariant);
    }

    @Override
    protected void fillLinks(final BreadcrumbViewModel model, final ProductWithVariant productWithVariant) {
        model.setLinks(createProductLinks(productWithVariant));
    }

    protected List<BreadcrumbLinkViewModel> createProductLinks(final ProductWithVariant productWithVariant) {
        final List<BreadcrumbLinkViewModel> categoryTreeLinks = createCategoryTreeLinks(productWithVariant);
        final List<BreadcrumbLinkViewModel> result = new ArrayList<>(1 + categoryTreeLinks.size());
        result.addAll(categoryTreeLinks);
        result.add(createProductLink(productWithVariant));
        return result;
    }

    private List<BreadcrumbLinkViewModel> createCategoryTreeLinks(final ProductWithVariant productWithVariant) {
        return productWithVariant.getProduct().getCategories().stream()
                .findFirst()
                .flatMap(ref -> getCategoryTree().findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    private BreadcrumbLinkViewModel createProductLink(final ProductWithVariant productWithVariant) {
        final BreadcrumbLinkViewModel linkViewModel = new BreadcrumbLinkViewModel();
        linkViewModel.setText(productWithVariant.getProduct().getName());
        linkViewModel.setUrl(getProductReverseRouter().productDetailPageUrlOrEmpty(productWithVariant.getProduct(), productWithVariant.getVariant()));
        return linkViewModel;
    }
}
