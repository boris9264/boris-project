package com.boris;

import com.boris.common.utils.JsonUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IndexTest {
    private static RestHighLevelClient client;
    static {
        try {
            //配置信息Settings自定义
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("127.0.0.1", 9201, "http"),
                            new HttpHost("127.0.0.1", 9202, "http")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建索引
    @Test
    public void ceateIndex() throws IOException {
        //索引名称
        CreateIndexRequest request = new CreateIndexRequest("film");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)  //主分片个数
                .put("index.number_of_replicas", 1) //副本分片个数，1代表为每个主分片各自生成一个副本分片
        );

        //索引mapping
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("american");
            {
                builder.startObject("properties");
                {
                    builder.startObject("name");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();
                    
                    builder.startObject("year");
                    {
                        builder.field("type", "integer");
                    }
                    builder.endObject();

                    builder.startObject("author");
                    {
                        builder.startObject("properties");
                        {
                            builder.startObject("firstName");
                            {
                                builder.field("type", "text");
                            }
                            builder.endObject();

                            builder.startObject("lastName");
                            {
                                builder.field("type", "text");
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        //索引类型名称
        request.mapping("american", builder);

        //创建索引别名
        request.alias(new Alias("twitter_alias").routing("").filter(QueryBuilders.termQuery("user", "kimchy")));

        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(JsonUtil.toString(createIndexResponse));
    }

    //查看索引
    @Test
    public void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest().indices("film");
        request.includeDefaults(true);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        GetIndexResponse getIndexResponse = client.indices().get(request, RequestOptions.DEFAULT);

        //film为索引名称
        ImmutableOpenMap<String, MappingMetaData> indexMappings = getIndexResponse.getMappings().get("film");

        //american为type名，根据type获取索引的mapping
        Map<String, Object> indexTypeMappings = indexMappings.get("american").getSourceAsMap();
        System.out.println(JsonUtil.toString(indexTypeMappings));

        //根据索引名称获取索引别名
        List<AliasMetaData> indexAliases = getIndexResponse.getAliases().get("film");
        if (!CollectionUtils.isEmpty(indexAliases)) {
            System.out.println(JsonUtil.toString(indexAliases));
        }

//        String numberOfShardsString = getIndexResponse.getSetting("film", "index.number_of_shards");


        Settings indexSettings = getIndexResponse.getSettings().get("film");

        Integer numberOfShards = indexSettings.getAsInt("index.number_of_shards", 0);
        System.out.println(numberOfShards);

        Integer numberOfRepShards = indexSettings.getAsInt("index.number_of_replicas", 0);
        System.out.println(numberOfRepShards);
    }

    //修改索引
    @Test
    public void updateIndex() throws IOException {
        //修改索引副本分片个数
        UpdateSettingsRequest request = new UpdateSettingsRequest("film");
        Settings settings = Settings.builder()
                        .put("index.number_of_replicas", 1)
                        .build();
        request.settings(settings);
        UpdateSettingsResponse updateSettingsResponse =
                client.indices().putSettings(request, RequestOptions.DEFAULT);
        System.out.println(updateSettingsResponse.isAcknowledged());
    }
}
