package com.example;



import com.alibaba.fastjson2.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class EsTestNew {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引 request  put test_index
     */
    @Test
    public void createIndex() throws IOException {
        // 1、创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("es_index");
        // 2、执行请求
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("createIndexResponse = " + createIndexResponse);
    }

    /**
     * 判断索引是否存在
     * @throws IOException
     */
    @Test
    public void existIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("es_index");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("exists = " + exists);
    }

    /**
     * 测试删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("zyg");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println("delete = " + delete);
    }

    /**
     * 搜索
     * searchRequest 搜索请求
     * sourceBuilder 条件构造
     * TermQueryBuilder 精确查询
     * MatchAllQueryBuilder 查询所有
     */
    @Test
    public void search1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        // 使用搜索条件构造器，构造搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //查询条件，使用QueryBuilders 工具类，来实现

        // QueryBuilders.termQuery 精确查询
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("username", "h");

        // QueryBuilders.matchAllQuery 匹配所有
		MatchAllQueryBuilder allQueryBuilder = QueryBuilders.matchAllQuery();

        sourceBuilder.query(allQueryBuilder);

        // 分页
		sourceBuilder.from(); // 默认 0
		sourceBuilder.size(); // 默认 10

        // 高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder(); //高亮构造器
		highlightBuilder.field("category"); //设置高亮字段
//		highlightBuilder.requireFieldMatch(false); // 关闭多个高亮
		highlightBuilder.preTags("<span style='color:red'>"); //设置前缀
		highlightBuilder.postTags("</span>"); //设置后缀
		sourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(sourceBuilder);

        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println("search.getHits() = " + JSON.toJSONString(search.getHits()));
        System.out.println("==============================");
        for (SearchHit hits:search.getHits().getHits()){
            System.out.println("hits.getSourceAsString() = " + hits.getSourceAsString());
        }
    }


}

