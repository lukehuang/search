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
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    /* 启动项目前同步ES */
    @Override
    public void run(ApplicationArguments args) {
        /* Product @Document注解，没有索引会自动创建 */
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
        productRepository.saveAll(productList);
    }

}
