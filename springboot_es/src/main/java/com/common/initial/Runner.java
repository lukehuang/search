package com.common.initial;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.module.demo.mapper.ProductMapper;
import com.module.demo.model.Product;
import com.module.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
public class Runner implements ApplicationRunner {

    @Value("${elasticsearch.index}")
    private String indexName;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    /* 启动项目前同步ES */
    @Override
    public void run(ApplicationArguments args) {
        try {
            if (!ESUtil.checkExistIndex(indexName)) {
                ESUtil.createIndex(indexName);
            }
            ESUtil.checkExistIndex(indexName);
            ESUtil.client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Product @Document注解，没有索引会自动创建 */
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
        long startTime = System.currentTimeMillis();
        productRepository.saveAll(productList);
        long times = System.currentTimeMillis() - startTime;
        System.out.println("插入:" + productList.size() + "条文档 耗时:" + times / 1000.0 + "秒");
    }

}
