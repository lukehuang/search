package com.util;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESIndexUtil {

    public static void main(String[] args) {
        try {
            String indexName = "how2java";
            if (!checkExistIndex(indexName)) {
                createIndex(indexName);
            }
            if (checkExistIndex(indexName)) {
                deleteIndex(indexName);
            }
            checkExistIndex(indexName);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************************************分割线************************************************************/
    /* todo ES索引管理 */

    /* 高可用RESTful ES Client */
    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

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
