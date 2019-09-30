package com.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Product {

    private Integer id;
    private String name;
    private String category;
    private float price;
    private String area;
    private String code;

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + ", area=" + area + ", code=" + code + "]";
    }

    public Map toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("category", category);
        map.put("price", price);
        map.put("area", area);
        map.put("code", code);
        return map;
    }

}
