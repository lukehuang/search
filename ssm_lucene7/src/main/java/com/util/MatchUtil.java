package com.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchUtil {

    public static void main(String[] args) throws IOException, ParseException {

        List<String> list1 = new ArrayList(); /* 查询数据（Lucene索引） */
        select("tuling_190330", "title", list1);

        IKAnalyzer analyzer = new IKAnalyzer();

        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);
        for (String title : list1) {
            Document doc = new Document();
            doc.add(new TextField("title", title, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();

        List<String> list2 = new ArrayList();
        select("tuling_190330_2", "title", list2); /* list1/list2符号.无法匹配？ */
        for (int i = 0; i < list2.size(); i++) {
            String keyword = list2.get(i);
            Query query = new QueryParser("title", analyzer).parse(keyword);

            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            int numberPerPage = 1000;
            ScoreDoc[] hits = searcher.search(query, numberPerPage).scoreDocs;

            Map map = new HashMap();
            map.put("title", list2.get(i));
            for (int j = 0; j < 3; j++) {
                ScoreDoc scoreDoc = hits[j];
                int docId = scoreDoc.doc;
                Document d = searcher.doc(docId);
                List<IndexableField> fields = d.getFields();
                for (int k = 0; k < fields.size(); k++) {
                    map.put("match_" + (j + 1), d.get(fields.get(k).name()));
                }
            }
            update("tuling_190330_2", map);
        }

    }

    static void select(String table, String column, List list) {
        String url = "jdbc:mysql://127.0.0.1:3306/untitled?useSSL=false&characterEncoding=utf-8";
        String username = "root";
        String password = "2468";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + table);
            while (resultSet.next()) {
                list.add(resultSet.getString(column));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void update(String table, Map map) {
        String url = "jdbc:mysql://127.0.0.1:3306/untitled?useSSL=false&characterEncoding=utf-8";
        String username = "root";
        String password = "2468";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("update " + table
                    + " set match_1='" + map.get("match_1")
                    + "',match_2='" + map.get("match_2")
                    + "',match_3='" + map.get("match_3")
                    + "' where title='" + map.get("title") + "'");
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
