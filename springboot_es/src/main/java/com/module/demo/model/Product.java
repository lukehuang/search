package com.module.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "how2java", type = "product") /* indexName（ES库名），type（ES表名） */
public class Product extends Model<Product> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String category;

    private Float price;

    private String area;

    private String code;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

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
