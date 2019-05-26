package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.mapper.ProductMapper;
import com.demo.model.Product;
import com.demo.util.ESUtil;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @ResponseBody
    @RequestMapping("/test")
    public String test() {
        String indexName = "how2java";

        /* 索引没有则创建，有则重建 */
        if (!ESUtil.checkExistIndex(indexName)) {
            System.out.println(ESUtil.createIndex(indexName));
        }
        if (ESUtil.checkExistIndex(indexName)) {
            System.out.println(ESUtil.deleteIndex(indexName));
            System.out.println(ESUtil.createIndex(indexName));
        }

        /* 文档增删改查 */
        Product product = new Product();
        product.setId(1);
        product.setName("product1");
        product.setArea("江苏南京");
        System.out.println(ESUtil.insertDocument(indexName, "product", product));
        System.out.println(ESUtil.getDocument(indexName, "product", product.getId()));
        product.setName("product2");
        System.out.println(ESUtil.updateDocument(indexName, "product", "name", product)); /* 只修改name字段 */
        System.out.println(ESUtil.getDocument(indexName, "product", product.getId()));
        System.out.println(ESUtil.deleteDocument(indexName, "product", product.getId()));

        /* 文档批量创建 */
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
        System.out.println(ESUtil.batchInsert(indexName, "product", productList));

        /* 文档分页查询 */
        String keyword = "时尚连衣裙";
        int start = 0;
        int count = 20; /* 每页条数 */
        SearchHits hits = ESUtil.search("how2java", "name", keyword, start, count);
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            System.out.println(hit.getSourceAsString());
        }

        return "";
    }

}
