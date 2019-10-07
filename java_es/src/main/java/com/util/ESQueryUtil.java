package com.util;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class ESQueryUtil {

    public static void main(String[] args) {
        query();
    }

    /************************************************************分割线************************************************************/
    /* todo ES查询操作 */

    /* 高可用RESTful ES Client */
    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

    /* 查询所有type？ */
    private static SearchHits search(String index, String filed, String keyword, int start, int count) throws IOException {
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
    }

    /************************************************************分割线************************************************************/

    /* ES分页查询 */
    private static void query() {
        try {
            String keyword = "时尚连衣裙";
            int start = 0;
            int count = 20; /* 每页条数 */
            SearchHits hits = search("how2java", "name", keyword, start, count);
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                System.out.println(hit.getSourceAsString());
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ES查询测速 */
    private static void query2() {
        String[] keywordArray = {
                "办公桌",
                "保暖内衣",
                "隐形眼镜",
                "防水瓷砖",
                "当当网",
                "液晶平板",
                "真皮皮鞋",
                "电饭锅",
                "指纹解锁",
                "时尚连衣裙"
        };
        try {
            for (String keyword : keywordArray) {
                long begin = System.currentTimeMillis();

                int start = 0;
                int count = 1;
                SearchHits hits = search("how2java", "name", keyword, start, count);
                SearchHit[] searchHits = hits.getHits();

                long times = System.currentTimeMillis() - begin;
                for (SearchHit hit : searchHits) {
                    System.out.println("耗时:" + times / 1000.0 + "秒 命中结果:" + hit.getSourceAsString());
                }
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
