package me.brilliant.system.modules.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.brilliant.boot.web.annotation.AnonymousGetMapping;
import me.brilliant.boot.web.mvc.BaseController;
import me.brilliant.system.modules.demo.dto.HotShopListDto;
import me.brilliant.system.modules.demo.dto.ProductItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/25
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "学习前端用的")
@RequestMapping("/api/shop")
public class JingdongDemoController extends BaseController {

    @AnonymousGetMapping("/hot-list")
    @Operation(summary = "首页的店铺列表")
    public List<HotShopListDto> get() {
        HotShopListDto item1 = HotShopListDto.builder()
                .id("1")
                .name("沃尔玛")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(10000)
                .expressLimit(0)
                .expressPrice(5)
                .slogan("VIP尊享满89元减4元运费").build();

        HotShopListDto item2 = HotShopListDto.builder()
                .id("2")
                .name("山姆会员商店")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(8000)
                .expressLimit(99)
                .expressPrice(15)
                .slogan("联合利华洗护满10减5").build();

        List<HotShopListDto> list = List.of(item1, item2);
        return list;
    }

    @AnonymousGetMapping("/{id}")
    @Operation(summary = "首页的详情")
    public HotShopListDto get(@PathVariable Long id) {
        HotShopListDto item1 = HotShopListDto.builder()
                .id("1")
                .name("沃尔玛")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(10000)
                .expressLimit(0)
                .expressPrice(5)
                .slogan("VIP尊享满89元减4元运费").build();

        HotShopListDto item2 = HotShopListDto.builder()
                .id("2")
                .name("山姆会员商店")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(8000)
                .expressLimit(99)
                .expressPrice(15)
                .slogan("联合利华洗护满10减5").build();
        if (id == 1) {
            return item1;
        } else {
            return item2;
        }
    }

    @AnonymousGetMapping("/{id}/products")
    @Operation(summary = "商品详情")
    public List<ProductItem> getProducts(@PathVariable Long id, @RequestParam String tab) {
        ProductItem item1 = ProductItem.builder()
                .id(1L)
                .name("番茄250g/份")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(10)
                .price(33.6f)
                .oldPrice(39.6f).build();

        ProductItem item2 = ProductItem.builder()
                .id(2L)
                .name("草莓250g/份")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(10)
                .price(33.6f)
                .oldPrice(39.6f).build();

        ProductItem item3 = ProductItem.builder()
                .id(3L)
                .name("提子250g/份")
                .imgUrl("http://www.dell-lee.com/imgs/vue3/near.png")
                .sales(10)
                .price(33.6f)
                .oldPrice(39.6f).build();
        List list = List.of(item1, item2, item3);
        return list;
    }

}
