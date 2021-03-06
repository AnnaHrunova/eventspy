package com.azure.eventmanager;

import com.azure.eventmanager.service.CoordinatesMessageListener;
import com.azure.eventmanager.service.EventManagerService;
import com.azure.eventmanager.service.MlService;
import com.microsoft.azure.eventgrid.TopicCredentials;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.azure.eventmanager.maps.AzureMapsClient;
import com.azure.eventmanager.maps.AzureMapsMockClient;

import lombok.val;

@Configuration
public class EventSpyConfig {

    private final static int HTTP_TIME_OUT = 5000;

    @ConditionalOnProperty(prefix = "azure", name = "maps.live.enabled", havingValue = "true")
    @Bean
    public AzureMapsClient azureMapsClient(final RestTemplateBuilder restTemplateBuilder,
                                           @Value("${azureMapsUrl}") String mapsUrl,
                                           @Value("${azureMapsSubscription}") String mapsSubscriptionKey) {
        return new AzureMapsClient(restTemplate(restTemplateBuilder), mapsUrl, mapsSubscriptionKey);
    }

    @Bean
    public MlService mlService(final RestTemplateBuilder restTemplateBuilder,
                               @Value("${mlScoreUrl}") String mlScoreUrl) {
        return new MlService(restTemplate(restTemplateBuilder), mlScoreUrl);
    }

    @Bean
    @ConditionalOnMissingBean(AzureMapsClient.class)
    public AzureMapsMockClient azureMapsMockClient() {
        return new AzureMapsMockClient();
    }


    @Bean
    public TopicCredentials topicCredentials(@Value("${eventGridKey}") String eventGridKey) {
        return new TopicCredentials(eventGridKey);
    }

    @Bean
    public CoordinatesMessageListener coordinatesMessageListener(EventManagerService eventManagerService) {
        return new CoordinatesMessageListener(eventManagerService);
    }

    @Bean
    public QueueClient queue(@Value("${queNameSpace}") String queNameSpace, EventManagerService eventManagerService) throws ServiceBusException, InterruptedException {
        QueueClient queueClient = new QueueClient(new ConnectionStringBuilder(queNameSpace, "coordinates"), ReceiveMode.PEEKLOCK);
        queueClient.registerMessageHandler(coordinatesMessageListener(eventManagerService));
        return queueClient;
    }

    private ClientHttpRequestFactory requestFactory() {
        val factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(HTTP_TIME_OUT);
        factory.setConnectTimeout(HTTP_TIME_OUT);
        factory.setReadTimeout(HTTP_TIME_OUT);
        return factory;
    }

    private RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> restTemplate.setRequestFactory(requestFactory());
    }

    private RestTemplate restTemplate(final RestTemplateBuilder builder) {
        builder.customizers(restTemplateCustomizer());
        return builder.build();
    }
}
