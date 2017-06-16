package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.AbstractBreadcrumbViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.BreadcrumbLinkViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.BreadcrumbViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductWithVariant;
import io.sphere.sdk.categories.CategoryTree;
import play.mvc.Call;

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
    protected BreadcrumbViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
        return new BreadcrumbViewModel();
    }

    @Override
    protected final void initialize(final BreadcrumbViewModel viewModel, final ProductWithVariant productWithVariant) {
        super.initialize(viewModel, productWithVariant);
    }

    @Override
    protected void fillLinks(final BreadcrumbViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setLinks(createProductLinks(productWithVariant));
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
        final String productUrl = getProductReverseRouter()
                .productDetailPageCall(productWithVariant.getProduct(), productWithVariant.getVariant())
                .map(Call::url)
                .orElse("");
        linkViewModel.setUrl(productUrl);
        return linkViewModel;
    }
}
