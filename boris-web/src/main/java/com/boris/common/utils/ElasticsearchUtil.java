package com.boris.common.utils;

import com.boris.vo.es.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ElasticsearchUtil {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    /**
     * @PostContruct是spring框架的注解
     * spring容器初始化的时候执行该方法
     */
    @PostConstruct
    public void init() {
        client = this.restHighLevelClient;
    }

    /**
     * 创建索引
     */
    public static boolean createIndex(String indexName) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );

        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            return createIndexResponse.isShardsAcknowledged();
        } catch (Exception e) {
            log.error("索引创建异常:", e);
            return false;
        }
    }

    /**
     * 删除索引
     */
    public static boolean deleteIndex(String indexName) {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            DeleteIndexResponse response = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (Exception e) {
            log.error("索引删除异常:", e);
            return false;
        }
    }

    /**
     * 向索引中添加数据
     * 数据中没有的属性不支持新增
     */
    public static boolean createOrUpdateDoc(ProductVo productVo) {
        IndexRequest request;


        if (StringUtils.isNotEmpty(productVo.getId())) {
            request = new IndexRequest(productVo.getIndexName(), productVo.getIndexType(), productVo.getId());
        } else {
            request = new IndexRequest(productVo.getIndexName(), productVo.getIndexType());
        }

        request.source(JsonUtil.toString(productVo), XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                return true;
            }
            if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        } catch (Exception e) {
            log.error("向索引中添加数据异常,参数:{}", JsonUtil.toString(productVo), e);
        }
        return false;
    }

    /**
     * 从索引中获取数据
     */
    public static ProductVo getDoc(ProductVo productVo) {
        GetRequest getRequest = new GetRequest(productVo.getIndexName(), productVo.getIndexType(), productVo.getId());
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                return JsonUtil.toPojo(getResponse.getSourceAsString(), ProductVo.class);
            }
        } catch (Exception e) {
            log.error("从索引中获取数据异常,参数:{}", JsonUtil.toString(productVo), e);
        }
        return null;
    }

    /**
     * 从索引中删除数据
     */
    public static boolean deleteDoc(ProductVo productVo) {
        DeleteRequest request = new DeleteRequest(productVo.getIndexName(), productVo.getIndexType(), productVo.getId());
        try {
            DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
            if (DocWriteResponse.Result.DELETED.equals(deleteResponse.getResult())) {
                return true;
            }

            if (deleteResponse.getShardInfo().getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : deleteResponse.getShardInfo().getFailures()) {
                    log.error("从索引中删除数据异常,异常原因:{}", failure.reason());
                }
            }
        } catch (Exception e) {
            log.error("从索引中删除数据异常,参数:{}", JsonUtil.toString(productVo), e);
        }
        return false;
    }

    /**
     * 从索引中批量更新数据
     */
    public static boolean bulkCreate(List<ProductVo> productVos) {
        BulkRequest request = new BulkRequest();
        for (ProductVo productVo : productVos) {
            request.add(new IndexRequest(productVo.getIndexName(), productVo.getIndexType(), productVo.getId())
                    .source(JsonUtil.toString(productVo), XContentType.JSON));
        }
        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        log.error("批量更新异常:{}", failure.getMessage());
                    }
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("从索引中批量更新数据异常,", e);
        }
        return false;
    }

    /**
     * 从索引中批量删除数据
     */
    public static boolean bulkDelete(List<ProductVo> productVos) {
        BulkRequest request = new BulkRequest();
        for (ProductVo productVo : productVos) {
            request.add(new DeleteRequest(productVo.getIndexName(), productVo.getIndexType(), productVo.getId()));
        }

        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        log.error("批量删除数据异常:{}", failure.getMessage());
                    }
                }
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("从索引中批量删除数据异常:", e);
        }
        return false;
    }

    public static List<Map<String,Object>> queryAll() {
        List<Map<String,Object>> results = new ArrayList<>();
        //构造器参数为索引名称，多个索引用逗号分隔
        SearchRequest searchRequest = new SearchRequest("products");
        //类型名称
        searchRequest.types("hea");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询全部 数据量较大时不能使用
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        //模糊查询
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("productName", "视")
                .fuzziness(Fuzziness.AUTO);
        searchSourceBuilder.query(matchQueryBuilder);

        //根据doc中的属性排序(如doc中没有该属性会过滤掉)
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.ASC));

        //从第几条数据开始查询
        searchSourceBuilder.from(0);
        //查询结果条数
        searchSourceBuilder.size(5);
        //设置超时时间
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            if (searchHits.getTotalHits() > 0) {
                for (SearchHit searchHit : searchHits) {
                    results.add(searchHit.getSourceAsMap());
                }
            }
        } catch (IOException e) {
            log.error("从索引中查询数据异常:", e);
        }
        return results;
    }

}
