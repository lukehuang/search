package com.module.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.module.demo.mapper.ProductMapper;
import com.module.demo.model.PageBean;
import com.module.demo.model.Product;
import com.module.demo.repository.ProductRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "name") String field,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer currentPage,
            HttpServletRequest request) {

        FunctionScoreQueryBuilder functionScoreQueryBuilder = null;
        if ("".equals(keyword)) {
            functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(QueryBuilders.matchAllQuery()); /* 查询所有 */
        } else {
            functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery(field, keyword)); /* 关键词查询 */
        }

        int pageSize = 10;
        int start = currentPage - 1; /* 开始页 */
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, pageSize, sort);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(functionScoreQueryBuilder)
                .withPageable(pageable)
                .build();

        /* 没有数据分页查询会报错 */
        Page<Product> productPage = productRepository.search(searchQuery);
        PageBean pageBean = new PageBean(productPage.getContent(), (int) productPage.getTotalElements(), pageSize, currentPage);
        pageBean.setPageURL(request.getRequestURI() + "?currentPage");

        ModelAndView view = new ModelAndView("/list");
        view.addObject("pageBean", pageBean);
        return view;
    }

    /* 新增/修改页 */
    @RequestMapping("/page")
    public ModelAndView page(@RequestParam(defaultValue = "0") Integer id) {
        Product product = productRepository.findById(id).get();

        ModelAndView view = new ModelAndView("/page");
        view.addObject("product", product);
        return view;
    }

    /* 响应新增/修改表单 */
    @RequestMapping("/insert")
    public ModelAndView insert(Product product) {
        productRepository.save(product); /* 修改时根据id覆盖？ */
        return new ModelAndView("redirect:/product/list");
    }

    @RequestMapping("/delete")
    public ModelAndView delete(Product product) {
        productRepository.delete(product);
        return new ModelAndView("redirect:/product/list");
    }

    /* 全部删除 */
    @RequestMapping("/deleteAll")
    public String deleteAll() {
        long count = productRepository.count();
        long startTime = System.currentTimeMillis();
        productRepository.deleteAll();
        long times = System.currentTimeMillis() - startTime;

        String msg = "删除:" + count + "条记录 耗时:" + times / 1000.0 + "秒";
        return msg;
    }

    /* 批量插入 */
    @RequestMapping("/batchInsert")
    public String batchInsert() {
        List<Product> productList = productMapper.selectList(new QueryWrapper<>());
        long startTime = System.currentTimeMillis();
        productRepository.saveAll(productList);
        long times = System.currentTimeMillis() - startTime;

        String msg = "插入:" + productList.size() + "条记录 耗时:" + times / 1000.0 + "秒";
        return msg;
    }

}
