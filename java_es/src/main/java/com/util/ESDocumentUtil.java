package com.util;

import com.model.Product;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ESDocumentUtil {

    /*
     * ①数据库角度：索引是数据库，type是表（product），根据id索引filed字段（name），每条文档是记录
     * ②Java角度：索引是List/Array，Product是类，根据id索引属性，每条文档是对象
     * */
    public static void main(String[] args) {
        try {
            /* 确保索引存在 */
            if (!checkExistIndex(indexName)) {
                createIndex(indexName);
            }
            /* 准备数据 */
            Product product = new Product();
            product.setId(2);
            product.setName("product1");

            insertDocument("product", "name", product);
            getDocument("product", product.getId());

            /* 修改文档 */
            product.setName("product2");
            updateDocument("product", "name", product);
            getDocument("product", product.getId());

            /* 删除文档（删除不存在的文档时没有提示） */
            deleteDocument("product", product.getId());
            getDocument("product", product.getId());

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************************************分割线************************************************************/
    /* todo ES文档管理 */

    /* 高可用RESTful ES Client */
    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

    /* 索引名 */
    private static String indexName = "how2java";

    private static void insertDocument(String type, String filed, Product product) {
        try {
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put(filed, product.getName()); /* 只索引一个字段 */
            IndexRequest indexRequest = new IndexRequest(indexName, type, String.valueOf(product.getId())).source(jsonMap);
            client.index(indexRequest);
            System.out.println("文档:" + product.getName() + " 插入成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文档:" + product.getName() + " 插入失败");
        }
    }

    private static void updateDocument(String type, String filed, Product product) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName, type, String.valueOf(product.getId())).doc(filed, product.getName());
            client.update(updateRequest);
            System.out.println("文档:" + product.getName() + " 修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文档:" + product.getName() + " 修改失败");
        }
    }

    private static void deleteDocument(String type, int id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, type, String.valueOf(id));
            client.delete(deleteRequest);
            System.out.println("id=" + id + " 的文档删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("id=" + id + " 的文档删除失败");
        }
    }

    private static void getDocument(String type, int id) {
        try {
            GetRequest request = new GetRequest(indexName, type, String.valueOf(id));
            GetResponse response = client.get(request);
            if (!response.isExists()) {
                System.out.println("id=" + id + " 的文档不存在");
            } else {
                String source = response.getSourceAsString();
                System.out.print("id=" + id + " 的文档内容是:");
                System.out.println(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************************************半分割线******************************/
    /* ES索引操作工具方法 */

    /* 创建索引 */
    private static void createIndex(String indexName) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            client.indices().create(request);
            System.out.println("索引:" + indexName + " 创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("索引:" + indexName + " 创建失败");
        }
    }

    /* 删除索引 */
    private static void deleteIndex(String indexName) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request);
            System.out.println("索引:" + indexName + " 删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("索引:" + indexName + " 删除失败");
        }
    }

    /* 判断索引是否存在 */
    private static boolean checkExistIndex(String indexName) throws IOException {
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
        }

        if (result) {
            System.out.println("索引:" + indexName + " 是存在的");
        } else {
            System.out.println("索引:" + indexName + " 不存在");
        }
        return result;
    }

}
