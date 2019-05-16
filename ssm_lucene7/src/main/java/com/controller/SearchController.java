package com.controller;

import com.mapper.TestMapper;
import com.model.Test;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lucene7")
public class SearchController {

    /************************************************************分割线************************************************************/

    /* todo Lucene 7.2.1 最简示例（单字段搜索） */
    @ResponseBody
    @RequestMapping("/search")
    public Map search() throws IOException, ParseException {

        /* 中文分词器 */
        IKAnalyzer analyzer = new IKAnalyzer();

        /* 准备数据 */
        List<String> productNames = new ArrayList();
        productNames.add("飞利浦led灯泡e27螺口暖白球泡灯家用照明超亮节能灯泡转色温灯泡");
        productNames.add("飞利浦led灯泡e14螺口蜡烛灯泡3W尖泡拉尾节能灯泡暖黄光源Lamp");
        productNames.add("雷士照明 LED灯泡 e27大螺口节能灯3W球泡灯 Lamp led节能灯泡");
        productNames.add("飞利浦 led灯泡 e27螺口家用3w暖白球泡灯节能灯5W灯泡LED单灯7w");
        productNames.add("飞利浦led小球泡e14螺口4.5w透明款led节能灯泡照明光源lamp单灯");
        productNames.add("飞利浦蒲公英护眼台灯工作学习阅读节能灯具30508带光源");
        productNames.add("欧普照明led灯泡蜡烛节能灯泡e14螺口球泡灯超亮照明单灯光源");
        productNames.add("欧普照明led灯泡节能灯泡超亮光源e14e27螺旋螺口小球泡暖黄家用");
        productNames.add("聚欧普照明led灯泡节能灯泡e27螺口球泡家用led照明单灯超亮光源");

        /* 创建索引 */
        Directory index = new RAMDirectory(); // 内存索引
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);
        for (String name : productNames) {
            Document doc = new Document();
            doc.add(new TextField("name", name, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();

        /* 搜索器 */
        String keyword = "护眼带光源"; // 模糊搜索关键词
        Query query = new QueryParser("name", analyzer).parse(keyword); // 根据关键词基于name字段搜索

        /* 搜索 */
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader); // searcher读取索引
        int numberPerPage = 1000; // 每页显示多少数据
        System.out.printf("当前一共有%d条数据%n", productNames.size());
        System.out.printf("搜索关键字是：\"%s\"%n", keyword);
        ScoreDoc[] hits = searcher.search(query, numberPerPage).scoreDocs; // 搜索核心方法

        /* 结果 */
        System.out.println("找到 " + hits.length + " 个命中.");
        System.out.println("序号\t匹配度得分\t结果");
        for (int i = 0; i < hits.length; ++i) {
            ScoreDoc scoreDoc = hits[i]; // 具体的搜索结果
            int docId = scoreDoc.doc; // 搜索结果在索引中的主键
            Document d = searcher.doc(docId); // productNames的索引形式
            List<IndexableField> fields = d.getFields();
            System.out.print((i + 1)); // 序号
            System.out.print("\t" + scoreDoc.score); // scoreDoc.score越高，匹配度越大
            for (IndexableField f : fields) {
                System.out.print("\t" + d.get(f.name()));
            }
            System.out.println();
        }

        /* 关闭搜索 */
        reader.close();

        Map result = new HashMap();
        return result;
    }

    /************************************************************分割线************************************************************/

    @Autowired
    private TestMapper testMapper;

    /*
     * todo 数据持久化
     * ①数据持久化（分词数据从MySQL中获取）
     * ②Util化（创建索引/展示结果独立出来）
     * ③高亮显示
     * */
    @ResponseBody
    @RequestMapping("/search2")
    public Map search2() throws IOException, ParseException, InvalidTokenOffsetsException {

        /* 中文分词器 */
        IKAnalyzer analyzer = new IKAnalyzer();

        /* 准备数据 */
        List<String> productNames = new ArrayList();
        String property = "property1"; // 搜索基于的字段
        List<Test> testList = testMapper.selectPropertyGroup(property);
        for (int j = 0; j < testList.size(); j++) {
            productNames.add(testList.get(j).getCommonProperty());
        }
        Directory index = createIndex(analyzer, productNames, property);

        /* 搜索器 */
        String keyword = "护眼带光源";
        Query query = new QueryParser(property, analyzer).parse(keyword);

        /* 搜索 */
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader); // searcher读取索引
        int numberPerPage = 1000; // 每页显示多少数据
        ScoreDoc[] hits = searcher.search(query, numberPerPage).scoreDocs; // 搜索核心方法

        /* 结果 */
        showSearchResults(hits, searcher, query, analyzer);

        /* 关闭搜索 */
        reader.close();

        Map result = new HashMap();
        return result;
    }

    /* 创建索引 */
    private static Directory createIndex(IKAnalyzer analyzer, List<String> productNames, String property) throws IOException {
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);
        for (String name : productNames) {
            Document doc = new Document();
            doc.add(new TextField(property, name, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();
        return index;
    }

    /* 展示结果 */
    private static void showSearchResults(ScoreDoc[] hits, IndexSearcher searcher, Query query, IKAnalyzer analyzer) throws IOException, InvalidTokenOffsetsException {
        System.out.println("序号\t匹配度得分\t结果<br>");

        /* 高亮命中结果 */
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

        for (int i = 0; i < hits.length; ++i) {
            ScoreDoc scoreDoc = hits[i]; // 具体的搜索结果
            int docId = scoreDoc.doc; // 搜索结果在索引中的主键
            Document d = searcher.doc(docId); // productNames的索引形式
            List<IndexableField> fields = d.getFields();
            System.out.print((i + 1)); // 序号
            System.out.print("\t" + scoreDoc.score); // scoreDoc.score越高，匹配度越大
            for (IndexableField f : fields) {
                TokenStream tokenStream = analyzer.tokenStream(f.name(), new StringReader(d.get(f.name())));
                String fieldContent = highlighter.getBestFragment(tokenStream, d.get(f.name()));
                System.out.print("\t" + fieldContent);
            }
            System.out.println("<br>");
        }
    }

    /************************************************************分割线************************************************************/

    /* todo 对搜索结果进行分页 */
    @ResponseBody
    @RequestMapping("/page")
    public Map page() throws IOException, ParseException, InvalidTokenOffsetsException {

        /* 中文分词器 */
        IKAnalyzer analyzer = new IKAnalyzer();

        /* 准备数据 */
        List<String> productNames = new ArrayList();
        String property = "property1"; // 搜索基于的字段
        List<Test> testList = testMapper.selectPropertyGroup(property);
        for (int j = 0; j < testList.size(); j++) {
            productNames.add(testList.get(j).getCommonProperty());
        }
        Directory index = createIndex(analyzer, productNames, property);

        /* 搜索器 */
        String keyword = "护眼带光源";
        Query query = new QueryParser(property, analyzer).parse(keyword);

        /* 搜索 */
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader); // searcher读取索引
        int pageCurrent = 3; // 页数
        int pageSize = 2; // 每页数据数
        ScoreDoc[] hits = pageSearch(query, searcher, pageCurrent, pageSize);

        /* 结果 */
        showSearchResults(hits, searcher, query, analyzer);

        /* 关闭搜索 */
        reader.close();

        Map result = new HashMap();
        return result;
    }

    /*
     * 分页搜索
     * ①搜索第91~第100条数据，先查第90条，再用searchAfter查91~100
     * */
    private static ScoreDoc[] pageSearch(Query query, IndexSearcher searcher, int pageCurrent, int pageSize) throws IOException {

        /* 如果pageCurrent != 1，start为pageCurrent前面所有页总数据数 */
        int start = (pageCurrent - 1) * pageSize;
        if (0 == start) {
            TopDocs topDocs = searcher.search(query, pageCurrent * pageSize);
            return topDocs.scoreDocs; // 返回第1页数据
        }

        TopDocs topDocs = searcher.search(query, start); // 搜索pageCurrent前面所有页（搜索相对topDocs.scoreDocs不耗时？）
        ScoreDoc preScore = topDocs.scoreDocs[start - 1]; // pageCurrent上一页的最后一条
        topDocs = searcher.searchAfter(preScore, query, pageSize);
        return topDocs.scoreDocs;
    }

}
