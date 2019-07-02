package com.util;

import com.model.Product;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ESBatchUtil {

    public static void main(String[] args) {
        try {
            if (!checkExistIndex(indexName)) {
                createIndex(indexName);
            }
            List<Product> productList = SQL2List("product");
            System.out.println("准备数据，总计" + productList.size() + "条");
            batchInsert("product", productList);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************************************分割线************************************************************/
    /* todo ES批量操作 */

    /* 高可用RESTful ES Client */
    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

    /* 索引名 */
    private static String indexName = "how2java";

    /* 批量插入文档 */
    private static void batchInsert(String type, List<Product> productList) {
        try {
            BulkRequest request = new BulkRequest();
            for (Product product : productList) {
                Map<String, Object> map = product.toMap(); /* Object转Map，索引所有字段 */
                IndexRequest indexRequest = new IndexRequest(indexName, type, String.valueOf(product.getId())).source(map);
                request.add(indexRequest);
            }
            client.bulk(request);
            System.out.println("批量插入完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************************************半分割线******************************/

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

    /************************************************************半分割线******************************/

    private static List<Product> file2List(String fileName) throws IOException {
        File file = new File(fileName);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = "";
        line = bufferedReader.readLine();

        List<Product> productList = new ArrayList<Product>();
        while (line != null) {
            Product product = line2product(line);
            productList.add(product);
            line = bufferedReader.readLine();
        }
        return productList;
    }

    private static Product line2product(String line) {
        Product product = new Product();
        String[] fields = line.split(",");
        product.setId(Integer.parseInt(fields[0]));
        product.setName(fields[1]);
        product.setCategory(fields[2]);
        product.setPrice(Float.parseFloat(fields[3]));
        product.setArea(fields[4]);
        product.setCode(fields[5]);
        return product;
    }

    private static List<Product> SQL2List(String tableName) {
        List<Product> productList = new ArrayList<Product>();

        String url = "jdbc:mysql://127.0.0.1:3306/untitled?useSSL=false";
        String username = "root";
        String password = "2468";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setCategory(resultSet.getString("category"));
                product.setPrice(resultSet.getFloat("price"));
                product.setArea(resultSet.getString("area"));
                product.setCode(resultSet.getString("code"));
                productList.add(product);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }

}
