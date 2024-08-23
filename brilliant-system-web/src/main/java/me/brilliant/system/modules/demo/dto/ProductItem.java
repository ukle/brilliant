package me.brilliant.system.modules.demo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/31
 */
@Data
@Builder
public class ProductItem {
    private Long id;
    private String name;
    private String imgUrl;
    private int sales;
    private float price;
    private float oldPrice;
}
