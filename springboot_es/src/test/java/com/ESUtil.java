package com;

import com.module.demo.model.Product;
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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESUtil {

    /*
     * HighLevelClient
     * ①RestClient即LowLevelClient，直接发送ES命令
     * ②HighLevelClient基于LowLevelClient
     * */
    public static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

    /************************************************************分割线************************************************************/

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

    /* 创建索引 */
    public static void createIndex(String indexName) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            client.indices().create(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 删除索引 */
    public static void deleteIndex(String indexName) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /************************************************************分割线************************************************************/

    /* 插入文档 */
    public static void insertDocument(String indexName, String typeName, String fieldName, Product product) {
        try {
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put(fieldName, product.getName()); /* 只索引一个字段 */
            IndexRequest indexRequest = new IndexRequest(indexName, typeName, String.valueOf(product.getId())).source(jsonMap);
            client.index(indexRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 修改文档 */
    public static void updateDocument(String indexName, String typeName, String fieldName, Product product) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName, typeName, String.valueOf(product.getId())).doc(fieldName, product.getName());
            client.update(updateRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 获取文档 */
    public static GetResponse getDocument(String indexName, String typeName, Integer id) {
        GetResponse response = null;
        try {
            GetRequest request = new GetRequest(indexName, typeName, String.valueOf(id));
            response = client.get(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /* 删除文档 */
    public static void deleteDocument(String indexName, String typeName, Integer id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, typeName, String.valueOf(id));
            client.delete(deleteRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 批量插入文档 */
    public static void batchInsert(String indexName, String typeName, List<Product> productList) {
        try {
            BulkRequest request = new BulkRequest();
            for (Product product : productList) {
                Map<String, Object> map = product.toMap(); /* Object转Map，索引所有字段 */
                IndexRequest indexRequest = new IndexRequest(indexName, typeName, String.valueOf(product.getId())).source(map);
                request.add(indexRequest);
            }
            client.bulk(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /************************************************************分割线************************************************************/

    /* 搜索 */
    public static void search(String indexName, String fieldName, String keyword, Integer currentPage, Integer pageSize) {
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(fieldName, keyword); /* 关键字匹配 */
            matchQueryBuilder.fuzziness(Fuzziness.AUTO); /* 模糊匹配 */
            sourceBuilder.query(matchQueryBuilder);
            sourceBuilder.from(currentPage - 1); /* 第几页 */
            sourceBuilder.size(pageSize); /* 第几条 */
            sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC)); /* 匹配度从高到低 */
            sourceBuilder.highlighter(new HighlightBuilder().preTags("<span style='color: red'>").postTags("</span>").field(fieldName)); /* 高亮 */

            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest);
            SearchHits hits = response.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
//                System.out.println(hit.getSourceAsString()); /* 查询title的结果 */
                System.out.println(hit.getHighlightFields()); /* 高亮 */

                /*
                 * ①整个Product对象
                 * fixme ②Map转对象（反射），再用高亮覆盖对应字段
                 * */
//                System.out.println(hit.getSourceAsMap());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
