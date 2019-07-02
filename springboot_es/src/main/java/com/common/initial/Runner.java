package com.common.initial;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.module.demo.mapper.ProductMapper;
import com.module.demo.model.Product;
import com.module.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
public class Runner implements ApplicationRunner {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    /* 启动项目前同步ES */
    @Override
    public void run(ApplicationArguments args) {
        /*
         * fixme 没有索引则创建
         * ①elasticsearch-rest-high-level-client与spring-data-elasticsearch自带的elasticsearch-rest-client冲突
         * ②使用elasticsearch-rest-client创建索引
         * */
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
        long startTime = System.currentTimeMillis();
        productRepository.saveAll(productList);
        long times = System.currentTimeMillis() - startTime;
        System.out.println("插入:" + productList.size() + "条文档 耗时:" + times / 1000.0 + "秒");
    }

}
