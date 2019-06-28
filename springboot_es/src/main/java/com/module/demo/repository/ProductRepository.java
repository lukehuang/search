package com.module.demo.repository;

import com.module.demo.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/* ES Repository */
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {

}
