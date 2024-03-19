package com.iksling.blog.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.connectTimeOut}")
    private int connectTimeOut;
    @Value("${elasticsearch.socketTimeOut}")
    private int socketTimeOut;
    @Value("${elasticsearch.connectionRequestTimeOut}")
    private int connectionRequestTimeOut;
    @Value("${elasticsearch.maxConnTotal}")
    private int maxConnTotal;
    @Value("${elasticsearch.maxConnPerRoute}")
    private int maxConnPerRoute;


    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost httpHost = new HttpHost(host, port);
        RestClientBuilder builder = RestClient.builder(httpHost);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(connectTimeOut)
                .setSocketTimeout(socketTimeOut)
                .setConnectionRequestTimeout(connectionRequestTimeOut)
        ).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute)
                .setDefaultCredentialsProvider(credentialsProvider)
        );
        return new RestHighLevelClient(builder);
    }
}
