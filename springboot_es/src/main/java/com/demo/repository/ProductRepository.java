package com.demo.repository;

import com.demo.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/* ES Repository */
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {

}
