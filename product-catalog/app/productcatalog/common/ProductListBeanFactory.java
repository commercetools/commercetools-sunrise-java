package productcatalog.common;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class ProductListBeanFactory {

    @Inject
    @Named("new")
    private CategoryTree categoryTreeInNew;
    @Inject
    private ProductBeanFactory productBeanFactory;

    public ProductListBean create(final Iterable<ProductProjection> productList) {
        final ProductListBean bean = new ProductListBean();
        bean.setList(StreamSupport.stream(productList.spliterator(), false)
                .map(product -> {
                    final ProductVariant matchingVariant = product.findFirstMatchingVariant()
                            .orElseGet(product::getMasterVariant);
                    return createThumbnail(product, matchingVariant);
                })
                .collect(toList()));
        return bean;
    }

    private ProductThumbnailBean createThumbnail(final ProductProjection product, final ProductVariant variant) {
        final ProductThumbnailBean bean = new ProductThumbnailBean();
        bean.setProduct(productBeanFactory.create(product, variant));
        bean.setNew(product.getCategories().stream()
                .anyMatch(category -> categoryTreeInNew.findById(category.getId()).isPresent()));
        fillIsSaleInfo(bean, bean.getProduct());
        return bean;
    }

    private static void fillIsSaleInfo(final ProductThumbnailBean bean, final ProductBean productBean) {
        final boolean isSale = productBean != null && productBean.getVariant() != null && productBean.getVariant().getPriceOld() != null;
        bean.setSale(isSale);
    }
}
