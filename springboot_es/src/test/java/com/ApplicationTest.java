package com;

import com.module.demo.mapper.ProductMapper;
import com.module.demo.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Value("${elasticsearch.indexName}")
    private String indexName;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    /* HighLevelClient测试 */
    @Test
    public void test() {
//        /* 索引 */
//        if (ESUtil.checkExistIndex(indexName)) {
//            ESUtil.deleteIndex(indexName);
//            ESUtil.createIndex(indexName);
//        } else {
//            ESUtil.createIndex(indexName);
//        }
//
//        /* 文档 */
//        Product product = Product.builder().id(1).name("name1").build();
//        ESUtil.insertDocument(indexName, "product", "name", product);
//
//        product.setName("name2");
//        ESUtil.updateDocument(indexName, "product", "name", product);
//
//        GetResponse response = ESUtil.getDocument(indexName, "product", product.getId());
//        if (response.isExists()) {
//            System.out.println(response.getSourceAsString());
//        }
//
//        ESUtil.deleteDocument(indexName, "product", product.getId());
//
//        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
//        ESUtil.batchInsert(indexName, "product", productList);

        String keyword = "瓷砖";
        ESUtil.search(indexName, "name", keyword, 1, 10);
    }

}
