package com.crawling.core.socket.config;

import com.tspoon.core.socket.service.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis WebSocket 설정 클래스
 * Redis Pub/Sub을 통한 실시간 메시지 전송을 위한 설정
 * 
 * @author tspoon
 * @version 1.0
 */
@Configuration
public class RedisWebSocketConfig {
    
    @Value("${spring.redis.channel.chat:chat}")
    private String chatChannel;
    
    @Value("${spring.redis.channel.notification:notification}")
    private String notificationChannel;
    
    /**
     * WebSocket용 Redis 템플릿을 설정합니다.
     * 
     * @param connectionFactory Redis 연결 팩토리
     * @return 설정된 Redis 템플릿
     */
    @Bean
    public RedisTemplate<String, Object> webSocketRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Key는 String으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // Value는 JSON으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.afterPropertiesSet();
        return template;
    }
    
    /**
     * 채팅 채널 토픽을 생성합니다.
     * 
     * @return 채팅 채널 토픽
     */
    @Bean
    public ChannelTopic chatTopic() {
        return new ChannelTopic(chatChannel);
    }
    
    /**
     * 알림 채널 토픽을 생성합니다.
     * 
     * @return 알림 채널 토픽
     */
    @Bean
    public ChannelTopic notificationTopic() {
        return new ChannelTopic(notificationChannel);
    }
    
    /**
     * Redis 메시지 리스너 컨테이너를 설정합니다.
     * 
     * @param connectionFactory Redis 연결 팩토리
     * @param messageListener 메시지 리스너 어댑터
     * @return 설정된 메시지 리스너 컨테이너
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListener) {
        
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListener, chatTopic());
        container.addMessageListener(messageListener, notificationTopic());
        
        return container;
    }
    
    /**
     * 메시지 리스너 어댑터를 생성합니다.
     * 
     * @param redisMessageSubscriber Redis 메시지 구독자
     * @return 메시지 리스너 어댑터
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisMessageSubscriber redisMessageSubscriber) {
        return new MessageListenerAdapter(redisMessageSubscriber, "onMessage");
    }
}
