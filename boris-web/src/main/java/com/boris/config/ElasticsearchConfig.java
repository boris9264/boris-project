package com.boris.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticsearchConfig {
    /**
     * elk集群地址
     */
    @Value("${elasticsearch.ip}")
    private String hostName;

    /**
     * 端口
     */
    @Value("${elasticsearch.port}")
    private Integer port;

    /**
     * 集群名称
     */
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    /**
     * 连接池
     */
    @Value("${elasticsearch.pool}")
    private String poolSize;

    /**
     * Bean name default  函数名字
     *
     * @return
     */
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient transportClient() {
        log.info("Elasticsearch初始化开始。。。。。");
        RestHighLevelClient restHighLevelClient = null;
        try {
            //配置信息Settings自定义
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(hostName, port, "http"),
                            new HttpHost(hostName, 9201, "http")));
        } catch (Exception e) {
            log.error("elasticsearch TransportClient create error!!", e);
        }
        return restHighLevelClient;
    }
}
