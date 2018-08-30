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
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

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
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );

        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request);
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
            DeleteIndexResponse response = client.indices().delete(deleteIndexRequest);
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
            request = new IndexRequest(productVo.getIndexName(), "doc", productVo.getId());
        } else {
            request = new IndexRequest(productVo.getIndexName(), "doc");
        }

        request.source(JsonUtil.toString(productVo), XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(request);
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
        GetRequest getRequest = new GetRequest(productVo.getIndexName(), "doc", productVo.getId());
        try {
            GetResponse getResponse = client.get(getRequest);
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
        DeleteRequest request = new DeleteRequest(productVo.getIndexName(), "doc", productVo.getId());
        try {
            DeleteResponse deleteResponse = client.delete(request);
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
            request.add(new IndexRequest(productVo.getIndexName(), "doc", productVo.getId())
                    .source(JsonUtil.toString(productVo), XContentType.JSON));
        }
        try {
            BulkResponse bulkResponse = client.bulk(request);
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
            request.add(new DeleteRequest(productVo.getIndexName(), "doc", productVo.getId()));
        }

        try {
            BulkResponse bulkResponse = client.bulk(request);
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
            log.error("从索引中批量删除数据异常,", e);
        }
        return false;
    }
//
//    /**
//     * 判断索引是否存在
//     *
//     * @param index
//     * @return
//     */
//    public static boolean isIndexExist(String index) {
//        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
//        if (inExistsResponse.isExists()) {
//            log.info("Index [" + index + "] is exist!");
//        } else {
//            log.info("Index [" + index + "] is not exist!");
//        }
//        return inExistsResponse.isExists();
//    }
//

//
//    /**
//     * 数据添加
//     *
//     * @param jsonObject 要增加的数据
//     * @param index      索引，类似数据库
//     * @param type       类型，类似表
//     * @return
//     */
//    public static String addData(JSONObject jsonObject, String index, String type) {
//        return addData(jsonObject, index, type, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
//    }
//
//    /**
//     * 通过ID删除数据
//     *
//     * @param index 索引，类似数据库
//     * @param type  类型，类似表
//     * @param id    数据ID
//     */
//    public static void deleteDataById(String index, String type, String id) {
//
//        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
//
//        log.info("deleteDataById response status:{},id:{}", response.status().getStatus(), response.getId());
//    }
//
//    /**
//     * 通过ID 更新数据
//     *
//     * @param jsonObject 要增加的数据
//     * @param index      索引，类似数据库
//     * @param type       类型，类似表
//     * @param id         数据ID
//     * @return
//     */
//    public static void updateDataById(JSONObject jsonObject, String index, String type, String id) {
//
//        UpdateRequest updateRequest = new UpdateRequest();
//
//        updateRequest.index(index).type(type).id(id).doc(jsonObject);
//
//        client.update(updateRequest);
//
//    }
//
//    /**
//     * 通过ID获取数据
//     *
//     * @param index  索引，类似数据库
//     * @param type   类型，类似表
//     * @param id     数据ID
//     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
//     * @return
//     */
//    public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {
//
//        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
//
//        if (StringUtils.isNotEmpty(fields)) {
//            getRequestBuilder.setFetchSource(fields.split(","), null);
//        }
//
//        GetResponse getResponse =  getRequestBuilder.execute().actionGet();
//
//        return getResponse.getSource();
//    }
//
//
//    /**
//     * 使用分词查询,并分页
//     *
//     * @param index          索引名称
//     * @param type           类型名称,可传入多个type逗号分隔
//     * @param startPage    当前页
//     * @param pageSize       每页显示条数
//     * @param query          查询条件
//     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
//     * @param sortField      排序字段
//     * @param highlightField 高亮字段
//     * @return
//     */
//    public static EsPage searchDataPage(String index, String type, int startPage, int pageSize, QueryBuilder query, String fields, String sortField, String highlightField) {
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
//        if (StringUtils.isNotEmpty(type)) {
//            searchRequestBuilder.setTypes(type.split(","));
//        }
//        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
//
//        // 需要显示的字段，逗号分隔（缺省为全部字段）
//        if (StringUtils.isNotEmpty(fields)) {
//            searchRequestBuilder.setFetchSource(fields.split(","), null);
//        }
//
////排序字段
//        if (StringUtils.isNotEmpty(sortField)) {
//            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
//        }
//
//// 高亮（xxx=111,aaa=222）
//        if (StringUtils.isNotEmpty(highlightField)) {
//            HighlightBuilder highlightBuilder = new HighlightBuilder();
//
//            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
//            //highlightBuilder.postTags("</span>");//设置后缀
//
//            // 设置高亮字段
//            highlightBuilder.field(highlightField);
//            searchRequestBuilder.highlighter(highlightBuilder);
//        }
//
////searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
//        searchRequestBuilder.setQuery(query);
//
//        // 分页应用
//        searchRequestBuilder.setFrom(startPage).setSize(pageSize);
//
//        // 设置是否按查询匹配度排序
//        searchRequestBuilder.setExplain(true);
//
//        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
//        log.info("\n{}", searchRequestBuilder);
//
//        // 执行搜索,返回搜索响应信息
//        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
//
//        long totalHits = searchResponse.getHits().totalHits;
//        long length = searchResponse.getHits().getHits().length;
//
//        log.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
//
//        if (searchResponse.status().getStatus() == 200) {
//// 解析对象
//            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);
//
//            return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
//        }
//
//        return null;
//
//    }
//
//
//    /**
//     * 使用分词查询
//     *
//     * @param index          索引名称
//     * @param type           类型名称,可传入多个type逗号分隔
//     * @param query          查询条件
//     * @param size           文档大小限制
//     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
//     * @param sortField      排序字段
//     * @param highlightField 高亮字段
//     * @return
//     */
//    public static List<Map<String, Object>> searchListData(String index, String type, QueryBuilder query, Integer size, String fields, String sortField, String highlightField) {
//
//        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
//        if (StringUtils.isNotEmpty(type)) {
//            searchRequestBuilder.setTypes(type.split(","));
//        }
//
//        if (StringUtils.isNotEmpty(highlightField)) {
//            HighlightBuilder highlightBuilder = new HighlightBuilder();
//            // 设置高亮字段
//            highlightBuilder.field(highlightField);
//            searchRequestBuilder.highlighter(highlightBuilder);
//        }
//
//        searchRequestBuilder.setQuery(query);
//
//        if (StringUtils.isNotEmpty(fields)) {
//            searchRequestBuilder.setFetchSource(fields.split(","), null);
//        }
//        searchRequestBuilder.setFetchSource(true);
//
//        if (StringUtils.isNotEmpty(sortField)) {
//            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
//        }
//
//        if (size != null && size > 0) {
//            searchRequestBuilder.setSize(size);
//        }
//
////打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
//        log.info("\n{}", searchRequestBuilder);
//
//        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
//
//        long totalHits = searchResponse.getHits().totalHits;
//        long length = searchResponse.getHits().getHits().length;
//
//        log.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
//
//        if (searchResponse.status().getStatus() == 200) {
//// 解析对象
//            return setSearchResponse(searchResponse, highlightField);
//        }
//
//        return null;
//
//    }
//
//
//
//    /**
//     * 高亮结果集 特殊处理
//     *
//     * @param searchResponse
//     * @param highlightField
//     */
//    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
//        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
//        StringBuffer stringBuffer = new StringBuffer();
//
//        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
//            searchHit.getSourceAsMap().put("id", searchHit.getId());
//
//            if (StringUtils.isNotEmpty(highlightField)) {
//
//                System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
//                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();
//
//                if (text != null) {
//                    for (Text str : text) {
//                        stringBuffer.append(str.string());
//                    }
////遍历 高亮结果集，覆盖 正常结果集
//                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
//                }
//            }
//            sourceList.add(searchHit.getSourceAsMap());
//        }
//
//        return sourceList;
//    }
}
