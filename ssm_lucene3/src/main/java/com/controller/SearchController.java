package com.controller;

import com.mapper.TestMapper;
import com.model.Test;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lucene3")
public class SearchController {

    @Autowired
    private TestMapper testMapper;

    /************************************************************分割线************************************************************/

    /* todo Lucene 3.6.0 本地索引示例 */
    @ResponseBody
    @RequestMapping("/search")
    public Map search() throws IOException, ParseException, InvalidTokenOffsetsException {

        /* 计时 */
        long a1 = System.currentTimeMillis();

        /* 本地索引配置 */
        FSDirectory directory = FSDirectory.open(new File("C:/Users/Administrator/Desktop/新建文件夹"));
        IKAnalyzer analyzer = new IKAnalyzer(true);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        /* 准备数据 */
        List<String> productNames = new ArrayList();
        String property = "property1"; /* 搜索基于的字段 */
        List<Test> testList = testMapper.selectPropertyGroup(property);
        for (int j = 0; j < testList.size(); j++) {
            productNames.add(testList.get(j).getCommonProperty());
        }

        /* 创建本地索引 */
        for (String name : productNames) {
            Document doc = new Document();
            doc.add(new Field(property, name, Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        writer.commit();
        writer.close();

        /* 创建索引的时间 */
        long b1 = System.currentTimeMillis();
        long c1 = b1 - a1;

        /* 搜索器 */
        IndexReader reader = IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader); /* 读取本地索引 */

        /* 搜索 */
        String keyword = "护眼带光源";
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, property, analyzer);
        Query query = queryParser.parse(keyword);
        ScoreDoc[] hits = searcher.search(query, 1000).scoreDocs; /* 搜索核心方法 */

        /* 结果 */
        showSearchResults(hits, searcher, query, analyzer);

        Map result = new HashMap();
        result.put("time:", c1 / 1000.0 + "s");
        return result;
    }

    /* 展示结果 */
    private static void showSearchResults(ScoreDoc[] hits, IndexSearcher searcher, Query query, IKAnalyzer analyzer) throws IOException, InvalidTokenOffsetsException {
        System.out.println("序号\t匹配度得分\t结果<br>");

        /* 高亮命中结果 */
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

        for (int i = 0; i < hits.length; ++i) {
            ScoreDoc scoreDoc = hits[i]; /* 具体的搜索结果 */
            int docId = scoreDoc.doc; /* 搜索结果在索引中的主键 */
            Document d = searcher.doc(docId); /* productNames的索引形式 */
            List<Fieldable> fields = d.getFields();
            System.out.print((i + 1)); /* 序号 */
            System.out.print("\t" + scoreDoc.score); /* scoreDoc.score越高，匹配度越大 */
            for (Fieldable f : fields) {
                System.out.print("\t" + d.get(f.name()));
                TokenStream tokenStream = analyzer.tokenStream(f.name(), new StringReader(d.get(f.name())));
                String fieldContent = highlighter.getBestFragment(tokenStream, d.get(f.name()));
                System.out.print("\t" + fieldContent);
            }
            System.out.println("<br>");
        }
    }

}
