package com.demo.util;

import com.demo.model.Product;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ESUtil {

    /* fixme 启动时从配置中读取ES配置 */
    static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

    /************************************************************分割线************************************************************/

    /* 创建索引 */
    public static String createIndex(String indexName) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            client.indices().create(request);
            return "索引:" + indexName + " 创建成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "索引:" + indexName + " 创建失败";
        }
    }

    /* 删除索引 */
    public static String deleteIndex(String indexName) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request);
            return "索引:" + indexName + " 删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "索引:" + indexName + " 删除失败";
        }
    }

    /* 判断索引是否存在 */
    public static boolean checkExistIndex(String indexName) {
        boolean result = true;
        try {
            OpenIndexRequest openIndexRequest = new OpenIndexRequest(indexName);
            client.indices().open(openIndexRequest).isAcknowledged();
        } catch (ElasticsearchStatusException e) {
            String msg = "Elasticsearch exception [type=index_not_found_exception, reason=no such index]";
            /* 如果捕获到相应错误，则说明索引不存在 */
            if (msg.equals(e.getMessage())) {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /************************************************************分割线************************************************************/

    /* 插入文档 */
    public static String insertDocument(String indexName, String type, Product product) {
        try {
            Map<String, Object> map = product.toMap(); /* Object转Map，索引所有字段 */
            IndexRequest indexRequest = new IndexRequest(indexName, type, String.valueOf(product.getId())).source(map);
            client.index(indexRequest);
            return "文档:" + product.getName() + " 插入成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "文档:" + product.getName() + " 插入失败";
        }
    }

    /* 修改文档 */
    public static String updateDocument(String indexName, String type, String filed, Product product) {
        try {
            /* fixme 根据id整体修改 */
            UpdateRequest updateRequest = new UpdateRequest(indexName, type, String.valueOf(product.getId())).doc(filed, product.getName());
            client.update(updateRequest);
            return "文档:" + product.getName() + " 修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "文档:" + product.getName() + " 修改失败";
        }
    }

    /* 删除文档 */
    public static String deleteDocument(String indexName, String type, int id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, type, String.valueOf(id));
            client.delete(deleteRequest);
            return "id=" + id + " 的文档删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "id=" + id + " 的文档删除失败";
        }
    }

    /* 获取文档 */
    public static String getDocument(String indexName, String type, int id) {
        try {
            GetRequest request = new GetRequest(indexName, type, String.valueOf(id));
            GetResponse response = client.get(request);
            if (!response.isExists()) {
                return null;
            } else {
                String source = response.getSourceAsString();
                return source;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /************************************************************分割线************************************************************/

    /* 批量插入文档 */
    public static String batchInsert(String indexName, String type, List<Product> productList) {
        try {
            BulkRequest request = new BulkRequest();
            for (Product product : productList) {
                Map<String, Object> map = product.toMap(); /* Object转Map，索引所有字段 */
                IndexRequest indexRequest = new IndexRequest(indexName, type, String.valueOf(product.getId())).source(map);
                request.add(indexRequest);
            }
            client.bulk(request);
            return "批量插入成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "批量插入失败";
        }
    }

    /************************************************************分割线************************************************************/

    /* 分页查询文档 */
    public static SearchHits search(String index, String filed, String keyword, int start, int count) {
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            /* 关键字匹配 */
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(filed, keyword);
            /* 模糊匹配 */
            matchQueryBuilder.fuzziness(Fuzziness.AUTO);
            sourceBuilder.query(matchQueryBuilder);
            /* 第几页 */
            sourceBuilder.from(start);
            /* 第几条 */
            sourceBuilder.size(count);
            searchRequest.source(sourceBuilder);
            /* 匹配度从高到低 */
            sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

            SearchResponse searchResponse = client.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            return hits;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
