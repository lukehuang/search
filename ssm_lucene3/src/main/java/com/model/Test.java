package com.model;

import lombok.Data;

@Data
public class Test {

    private Integer id;

    private String property1;

    private String property2;

    private String property3;

    /* 通用属性值，搭配MyBatis resultMap使用 */
    private String commonProperty;

}
