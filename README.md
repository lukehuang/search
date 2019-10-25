# search
搜索/Lucene/ES<br>

************************************************************************************************************************

# ssm_search
①SSM（JDK6）<br>
②SQL搜索（与Lucene进行对比）<br>

******************************************************************************************

# ssm_lucene3
①SSM+Lucene3（JDK6）<br>
②Lucene3示例（本地索引）<br>

# ssm_lucene7
①SSM+Lucene7（JDK8）<br>
②Lucene7示例（内存索引）<br>

******************************************************************************************

# springboot_es
①JDK8<br>
②MBP+ES6.2.2+spring-data-elasticsearch3.1.6+elasticsearch-rest-high-level-client6.4.3（HttpClient4.4）<br>
③spring-data-elasticsearch<br>
spring-data-elasticsearch通过@Document+ES Repository可以实现直接对ES快速CURD（端口9300）<br>
spring-data-elasticsearch到3.2.x（还未发布）才支持ES6.2.3及以上版本<br>
④HighLevelClient<br>
transport通过TCP方式访问ES（只支持Java/端口9300）<br>
rest-client/rest-high-level-client通过HTTP方式访问ES（无语言限制/端口9200）<br>
⑤功能<br>
initial：启动项目前同步ES<br>
demo：CURD+分页+查询<br>

******************************************************************************************

# springboot_tika
①JDK8<br>
②Tika内容抽取<br>