package com.boris;

import com.boris.common.utils.JsonUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class ElasticsearchTest {
    private static RestHighLevelClient client;
    static {
        try {
            //配置信息Settings自定义
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("127.0.0.1", 9200, "http"),
                            new HttpHost("127.0.0.1", 9201, "http")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //集群健康状况
    @Test
    public void clusterHealth(){
        ClusterHealthRequest healthRequest = new ClusterHealthRequest();
        try {
            ClusterHealthResponse response = client.cluster().health(healthRequest, RequestOptions.DEFAULT);
            System.out.println(JsonUtil.toString(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //索引情况
    @Test
    public void indexInfo() throws IOException {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices("products");
        request.indicesOptions(IndicesOptions.lenientExpandOpen());

        GetMappingsResponse getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> allMappings = getMappingResponse.mappings();
        System.out.println(JsonUtil.toString(allMappings));
        MappingMetaData typeMapping = allMappings.get("products").get("hea");
        System.out.println(JsonUtil.toString(typeMapping));
        Map<String, Object> tweetMapping = typeMapping.sourceAsMap();
        System.out.println(JsonUtil.toString(tweetMapping));
        System.out.println(JsonUtil.toString(getMappingResponse));
    }

}
