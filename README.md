# search
搜索/Lucene/ES<br>

************************************************************************************************************************

# ssm_search
①SSM（JDK6）<br>
②SQL搜索（与Lucene进行对比）<br>

# ssm_lucene3
①SSM+Lucene3（JDK6）<br>
②Lucene3示例（本地索引）<br>

# ssm_lucene7
①SSM+Lucene7（JDK8）<br>
②Lucene7示例（内存索引）<br>

# java_es
①JDK8<br>
②elasticsearch-rest-high-level-client6.2.2+HttpClient4.4（必须是4.4）+ES6.2.2<br>

③How2J ES示例<br>
ESIndexUtil（ES索引管理）<br>
ESDocumentUtil（ES文档管理）<br>
ESBatchUtil（ES批量操作）<br>
ESQueryUtil（ES查询操作）<br>
④技术细节<br>
transport通过TCP方式访问ES（只支持Java/端口9300）<br>
high-level-client通过HTTP方式访问ES（无语言限制/端口9200）<br>
Java Web项目的持久层看作数据库和项目之间的中间件，ES模块看作ES和项目之间的中间件（ES是另一种"数据库"）<br>

# ssm_es
①JDK8<br>
②SS+MBP+elasticsearch-rest-high-level-client6.2.2+ES6.2.2<br>

③ES中间件<br>
通过high-level-client与ES交互（端口9200），需要手写CURD<br>
查看/查询的数据源为ES，增删改的数据源为MySQL（不推荐用ES代替MySQL作为持久层）<br>

# springboot_es
①JDK8<br>
②SpringBoot2.1.3+MBP+spring-data-elasticsearch3.1.6+ES6.2.2<br>

③How2J SpringBoot ES示例<br>
④spring-data-elasticsearch<br>
spring-data-elasticsearch通过@Document+ES Repository可以实现直接对ES快速CURD（端口9300）<br>
spring-data-elasticsearch到3.2.x（还未发布）才支持ES6.2.3及以上版本<br>
⑤业务<br>
demo包：CURD+分页+查询<br>

************************************************************************************************************************

# 参考
①https://blog.wizzer.cn/archives/2299（Lucene3）<br>
②http://how2j.cn/k/search-engine/search-engine-intro/1672.html（Lucene7）<br>
③http://how2j.cn/k/search-engine/search-engine-java-api-index/1700.html（ES）<br>